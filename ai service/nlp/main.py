from Questgen import main
import nltk
import random
import pika, sys, os
import json

nltk.download('stopwords')
qg = main.QGen()


class Question:
    def __init__(self, question_statement, options, correct_option_index, text_data_id):
        self.questionStatement = question_statement
        self.options = options
        self.correctChoiceIndex = correct_option_index
        self.textDataId = text_data_id

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)


def generate_question(input_text):
    payload = {
        "input_text": input_text
    }
    output = qg.predict_mcq(payload)
    return output['questions']


def callback(ch, method, properties, body):
    body = body.decode("utf-8")
    x = body.split("#*$*#")
    text = x[0]
    text_data_id = x[1]

    text = text.replace("\"", "")
    text_data_id = text_data_id.replace("\"", "")

    print("Received: ", text, "\nText Data ID: ", text_data_id)

    generated_question_list = generate_question(text)
    for question in generated_question_list:
        question_statement = question['question_statement']
        options_list = question['options']
        random_option_index = random.randint(0, len(question['options']))
        options_list.insert(random_option_index, question['answer'])
        generated_question = Question(question_statement, options_list, random_option_index, text_data_id)

        sender_connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        sender_channel = sender_connection.channel()
        data = generated_question.toJSON()
        sender_channel.basic_publish(exchange='', routing_key='question', body=data)
        print("PUBLISHED")

    print("Completed Generating questions from text data with ID: " + text_data_id)


def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()
    channel.basic_consume(queue='text', auto_ack=True, on_message_callback=callback)
    channel.start_consuming()


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)

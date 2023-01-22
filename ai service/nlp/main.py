from Questgen import main
import nltk
import random
import pika, sys, os
import json

nltk.download('stopwords')
qg = main.QGen()


class Question:
    def __init__(self, question_statement, options, correct_option_index, user_id):
        self.questionStatement = question_statement
        self.options = options
        self.correctChoiceIndex = correct_option_index
        self.userId = user_id

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)


def generate_question(input_text, user_id):
    payload = {
        "input_text": input_text
    }
    output = qg.predict_mcq(payload)

    random_question_index = random.randint(0, len(output['questions'])) - 1

    question_statement = output['questions'][random_question_index]['question_statement']
    options_list = output['questions'][random_question_index]['options']
    random_option_index = random.randint(0, len(output['questions'][random_question_index]['options']))
    options_list.insert(random_option_index, output['questions'][random_question_index]['answer'])
    generated_question = Question(question_statement, options_list, random_option_index, user_id)
    return generated_question


def callback(ch, method, properties, body):
    body = body.decode("utf-8")
    x = body.split("#*$*#")
    text = x[0]
    user_id = x[1]

    text = text.replace("\"", "")
    user_id = user_id.replace("\"", "")

    print("Received: ", text, "\nUser ID: ", user_id)
    generated_question = generate_question(text, user_id)

    sender_connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    sender_channel = sender_connection.channel()

    # sender_channel.queue_declare(queue='question')
    data = generated_question.toJSON()
    sender_channel.basic_publish(exchange='', routing_key='question', body=data)
    print("PUBLISHED")


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

from Questgen import main
import nltk
nltk.download('stopwords')

qg = main.QGen()

payload = {
            "input_text": "This three-bolt diving helmet and suit were made in Russia in 1983. The basic design of the helmet is similar to the British Siebe-Gorman 12-bolt from the 1840s, and is still in production (for collectors) today! It is designed to be used with compressed air, not the more sophisticated mixed-gas technology used for deeper diving"
        }

output = qg.predict_mcq(payload)
print(output)
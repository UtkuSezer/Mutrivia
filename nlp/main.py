from Questgen import main
import nltk
import sys
import json
nltk.download('stopwords')

qg = main.QGen()
#open text file
end = False
with open("questions.txt", "w") as f:
    while(not end):
        f.write("\n")
        text = input("enter a text:")
        payload = {
                    "input_text": text
                }
        f.write("statement:\t")
        output1 = qg.predict_mcq(payload)
        f.write(output1['statement'])
        f.write("\n")
        answer_opt = ""
        print("\nstatement:", output1['statement'])
        for key, value in output1['questions'][0].items():
            if(key == "answer"):
                answer_opt = output1['questions'][0]["answer"]
            elif(key == "options"):
                print("options:")
                f.write("options:")
                f.write("\n")
                for i in range(len(output1['questions'][0]["options"])):
                    f.write(str(i+1) + ") " + output1['questions'][0]["options"][i])
                    f.write("\n")
                    print(str(i+1) + ") " + output1['questions'][0]["options"][i])
                f.write("*" + str(i+2) + ") " + answer_opt)
                f.write("\n")
                print("*" + str(i+2) + ") " + answer_opt)
            elif(key == "question_type" or key == "id" or key == "options_algorithm"):
                continue
            else:
                f.write(key + " :\t " + str(value))
                f.write("\n")
                print(key, ":", value)
        print("time taken to produce questions:\n" + str(output1['time_taken']) + " seconds")
        f.write("time taken to produce questions:\t" + str(output1['time_taken']) + " seconds")
        f.write("\n")
        print("***********************************************************")
        f.write("\n")
        f.write("***********************************************************")
        f.write("\n")
        finish = input("do you want to generate another question [y/n]? ")
        if finish == "n":
            end = True
        else:
            continue
"""
This three-bolt diving helmet and suit were made in Russia in 1983. The basic design of the helmet is similar to the British Siebe-Gorman 12-bolt from the 1840s, and is still in production (for collectors) today! It is designed to be used with compressed air, not the more sophisticated mixed-gas technology used for deeper diving.
In order to navigate at sea, one must know the true time as well as the angular measurement to sun or stars. The marine chronometer is especially designed to keep accurate time at sea, despite the stresses of motion and temperature change.Keeps on running precisely one day.                           
"""

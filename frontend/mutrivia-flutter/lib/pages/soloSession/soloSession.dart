import 'package:flutter/material.dart';
import 'package:mutrivia_flutter/pages/startQuiz.dart';
import 'package:http/http.dart' as http;
class SoloSession extends StatefulWidget {
  const SoloSession({Key? key}) : super(key: key);

  @override
  State<SoloSession> createState() => _SoloSessionState();
}

class _SoloSessionState extends State<SoloSession> {
  TextEditingController museumID = TextEditingController();
  bool _errorText = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        automaticallyImplyLeading: false,
        centerTitle: true,
        title: Row(
          mainAxisAlignment: MainAxisAlignment.center, //Center Column contents vertically,
          children: <Widget>[
            Image.asset('assets/images/favicon_white.png', height: 40, width: 40,),
            const Text('Mutrivia'),
          ],
        ),
        backgroundColor: Colors.deepPurple[500],
      ),
      body: Container(
        alignment: Alignment.center,
        child: SafeArea(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 200.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text("Enter Museum ID below", style: TextStyle(
                    color: Colors.black,
                    fontSize: 15
                ),
                ),
                SizedBox(height: 30),
                TextField(
                  controller: museumID,
                  decoration: InputDecoration(
                    filled: true,
                    fillColor: Colors.deepPurple[50],
                    hintText: "Museum ID",
                    errorText: _errorText ? "Museum ID can't be empty" : null,
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(12),
                      ),
                    ),
                  ),
                ),
                Padding(
                  padding: EdgeInsets.fromLTRB(0, 30, 0, 0),
                  child: SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        textStyle: const TextStyle(fontSize: 20),
                        primary: Colors.deepPurple[400],
                        onPrimary: Colors.white,
                        onSurface: Colors.grey,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                      ),
                      onPressed: () {
                        setState(() {
                          museumID.text.isEmpty ? _errorText = true : _errorText = false;

                          if (_errorText == false) {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                        StartQuiz()));
                          }
                        });
                      },
                      child: const Text('Start Quiz'),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

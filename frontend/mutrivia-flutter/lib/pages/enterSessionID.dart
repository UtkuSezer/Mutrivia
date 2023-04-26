import 'package:flutter/material.dart';

class EnterSessionID extends StatefulWidget {
  const EnterSessionID({Key? key}) : super(key: key);

  @override
  State<EnterSessionID> createState() => _EnterSessionIDState();
}

class _EnterSessionIDState extends State<EnterSessionID> {
  TextEditingController sessionID = TextEditingController();
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
                Text("Enter Session ID below", style: TextStyle(
                    color: Colors.black,
                    fontSize: 15
                ),
                ),
                SizedBox(height: 30),
                TextField(
                  controller: sessionID,
                  decoration: InputDecoration(
                    filled: true,
                    fillColor: Colors.deepPurple[50],
                    hintText: "Session ID",
                    errorText: _errorText ? "Session ID can't be empty" : null,
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
                          sessionID.text.isEmpty ? _errorText = true : _errorText = false;

                          if (_errorText == false) {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                        Placeholder()));
                          }
                        });
                      },
                      child: const Text('Enter'),
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
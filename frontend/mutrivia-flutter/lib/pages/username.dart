import 'package:flutter/material.dart';
import "package:websafe_svg/websafe_svg.dart";
import "package:mutrivia_flutter/pages/options.dart";
import "package:mutrivia_flutter/classes/user-data.service.dart";
import 'package:mutrivia_flutter/models/user.dart';

class WelcomeScreen extends StatefulWidget {
  const WelcomeScreen({Key? key}) : super(key: key);

  @override
  State<WelcomeScreen> createState() => _WelcomeScreenState();
}

class _WelcomeScreenState extends State<WelcomeScreen> {
  TextEditingController usernameController = TextEditingController();

  bool _errorText = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: Colors.deepPurple[600],
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 200.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Align(alignment: Alignment.center,
                child: Image.asset('assets/images/favicon_white.png', height: 50, width: 50,),),
              Text(
                'Welcome to Mutrivia!',
                style: TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 30
                ),
              ),
              Text("Enter your username below", style: TextStyle(
                  color: Colors.white,
                  fontSize: 15
              ),
              ),
              SizedBox(height: 30),
              TextField(
                controller: usernameController,
                decoration: InputDecoration(
                  filled: true,
                  fillColor: Colors.deepPurple[50],
                  hintText: "Username",
                  errorText: _errorText ? "Username can't be empty" : null,
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
                      primary: Colors.deepPurple[700],
                      onPrimary: Colors.white,
                      onSurface: Colors.grey,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                    ),
                    onPressed: () {
                      setState(() {
                        usernameController.text.isEmpty ? _errorText = true : _errorText = false;

                        var userId;
                        if (_errorText == false) {
                          addUser(usernameController.text);

                          Future<User> resultUser = addUser(usernameController.text);
                          resultUser.then((value){
                            userId = value.userId;
                            print("alooo $userId");

                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                        OptionsScreen(
                                            username: usernameController.text,
                                            userId: userId
                                          )));
                          } //resultUser
                          );
                        }
                      });
                    },
                    child: const Text('Submit'),
                  ),
                ),
              ),
              Spacer(),
            ],
          ),
        ),
      ),
    );
  }
}


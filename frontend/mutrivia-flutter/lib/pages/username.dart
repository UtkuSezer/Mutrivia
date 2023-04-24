import 'package:flutter/material.dart';
import "package:websafe_svg/websafe_svg.dart";
import "package:mutrivia_flutter/pages/options.dart";

class WelcomeScreen extends StatelessWidget {
  const WelcomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
              Spacer(),
              TextField(
                  decoration: InputDecoration(
                      filled: true,
                      fillColor: Colors.deepPurple[50],
                      hintText: "Username",
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
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) =>
                                    const OptionsScreen()));
                      }, //burda host olma, ya da odaya katılma veya tek oynama gibi seçenekleri seçeceği sayfaya gitmeli basınca yani on pressed
                      child: const Text('THANK YOU NEXT'),
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

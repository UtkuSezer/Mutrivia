import 'package:flutter/material.dart';
import "package:mutrivia_flutter/pages/loading.dart";
import 'package:mutrivia_flutter/pages/quiz.dart';
import 'package:mutrivia_flutter/pages/startQuiz.dart';
import 'package:mutrivia_flutter/pages/enterSessionID.dart';
import 'package:mutrivia_flutter/pages/hostSession.dart';
import 'package:mutrivia_flutter/pages/soloSession.dart';
import 'package:http/http.dart' as http;

class OptionsScreen extends StatelessWidget {
  final String username;
  OptionsScreen({Key? key, required this.username}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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

      body: Center(
        child: Padding(
          padding: EdgeInsets.symmetric(vertical: 40.0, horizontal: 15.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Expanded(
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                       style: ElevatedButton.styleFrom(
                         textStyle: const TextStyle(fontSize: 25),
                         primary: Colors.deepPurple[700],
                         onPrimary: Colors.white,
                         onSurface: Colors.grey,
                         shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                       ),
                       onPressed: () {
                         Navigator.push(
                             context,
                             MaterialPageRoute(
                                 builder: (context) => HostSession()));
                       }, //burda host olma, ya da odaya katılma veya tek oynama gibi seçenekleri seçeceği sayfaya gitmeli basınca yani on pressed
                       child: Text('HOST A SESSION'), //host a session
                     ),
                ),
              ),
              SizedBox(height: 20),
              Expanded(
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    textStyle: const TextStyle(fontSize: 25),
                    primary: Colors.deepPurple[400],
                    onPrimary: Colors.white,
                    onSurface: Colors.grey,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  ),
                  onPressed: () {
                    Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => EnterSessionID()));
                  }, //burda host olma, ya da odaya katılma veya tek oynama gibi seçenekleri seçeceği sayfaya gitmeli basınca yani on pressed
                  child: Text('JOIN A SESSION'),
                    ),
                ),
              ),
              SizedBox(height: 20),
              Expanded(
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    textStyle: const TextStyle(fontSize: 25),
                    primary: Colors.deepPurple[100],
                    onPrimary: Colors.white,
                    onSurface: Colors.grey,
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  ),
                  onPressed: () {
                    Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) => SoloSession()));
                  }, //burda host olma, ya da odaya katılma veya tek oynama gibi seçenekleri seçeceği sayfaya gitmeli basınca yani on pressed
                  child: Text('PLAY SOLO'),
              ),
                ),
              ),
            ],
          ),
        ),
      ),

    );
  }
}
import 'package:flutter/material.dart';
import 'package:mutrivia_flutter/pages/username.dart';
import 'package:http/http.dart' as http;
void main() => runApp(Home());

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Mutrivia',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        fontFamily: 'Nunito',
      ),
      home: WelcomeScreen()
    );
  }
}

/*
class Body extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Row(
          mainAxisAlignment: MainAxisAlignment.center, // Center Row contents horizontally
          children: <Widget>[
            Image.asset('assets/images/favicon_white.png', height: 40, width: 40,),
            const Text('Mutrivia'),
          ],
        ),
        centerTitle: true,
        backgroundColor: Colors.deepPurple[500],
      ),

      body: const Center(
        child: Icon(
          Icons.account_balance_outlined,
          size: 150.0,
        ),
      ),

      floatingActionButton: FloatingActionButton(
        onPressed: () {},
        backgroundColor: Colors.deepPurple[500],
        child: const Text(
          'Submit',
          style: TextStyle(
              fontSize: 12.0
          ),
        ),
      ),
    );
  }
}
*/
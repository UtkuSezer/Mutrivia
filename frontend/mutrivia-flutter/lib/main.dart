import 'package:flutter/material.dart';

void main() => runApp(MaterialApp(
  home: Body(),
  theme: ThemeData(fontFamily: 'Nunito'),
));

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

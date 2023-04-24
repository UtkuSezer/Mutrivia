import 'package:flutter/material.dart';

void main() => runApp(MaterialApp(
  home: Scaffold(
    appBar: AppBar(
      title: Text('Mutrivia'),
      centerTitle: true,
      backgroundColor: Colors.deepPurple[500],
    ),

    body: Center(
      child: Text('Hello World'),
    ),

    floatingActionButton: FloatingActionButton(
      onPressed: () {},
      child: Text(
          'Submit',
        style: TextStyle(
          fontSize: 12.0
        ),
      ),
      backgroundColor: Colors.deepPurple[500],
    ),
  ),

  theme: ThemeData(
    fontFamily: 'Nunito'
  ),
));


import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:http/http.dart' as http;
class Loading extends StatefulWidget {
  @override
  _LoadingState createState() => _LoadingState();
}

class _LoadingState extends State<Loading> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.purple[900],
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SpinKitCircle(
              color: Colors.white,
              size: 50.0,
            ),
            SizedBox(height: 4,),
            Text(
              "Waiting for host to start the quiz",
              style: TextStyle(
                fontSize: 20,
                color: Colors.white
              ),
            )
          ],
        )
      )
    );
  }
}
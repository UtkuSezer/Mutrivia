import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:http/http.dart' as http;
class ParticipantSessionWaiting extends StatefulWidget {

  const ParticipantSessionWaiting ({Key? key, required this.userId, required this.username,required this.sessionId}) : super(key: key);
  final String userId;
  final String username;
  final String sessionId;

  @override
  _ParticipantSessionWaitingState createState() => _ParticipantSessionWaitingState(userId: userId, username: username, sessionId: sessionId);

}

class _ParticipantSessionWaitingState extends State<ParticipantSessionWaiting> {

  final String userId;
  final String username;
  final String sessionId;

  _ParticipantSessionWaitingState({required this.userId, required this.username, required this.sessionId});

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
            ),
            Text(
                          "$username",
                          style: TextStyle(
                            fontSize: 20,
                            color: Colors.white
                          ),
                        ),
            Text(
                         "$sessionId",
                          style: TextStyle(
                            fontSize: 20,
                            color: Colors.white
                          ),
                        ),
             Text(
                       "$userId",
                          style: TextStyle(
                             fontSize: 20,
                             color: Colors.white
                           ),
                         ),
          ],
        )
      )
    );
  }
}
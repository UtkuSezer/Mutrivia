import 'package:flutter/material.dart';

class soloQuiz extends StatefulWidget {
  const soloQuiz({Key? key, required this.userId, required this.username,required this.sessionId}) : super(key: key);
  final String userId;
  final String username;
  final String sessionId;

  @override
  State<soloQuiz> createState() => _soloQuizState(userId: userId, username: username, sessionId: sessionId);
}

class _soloQuizState extends State<soloQuiz> {

  final String userId;
  final String username;
  final String sessionId;

  _soloQuizState({required this.userId, required this.username, required this.sessionId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.purple[900],
        body: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(height: 4,),
                Text(
                  "$username",
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
                Text(
                                  "$sessionId",
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

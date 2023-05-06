import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:mutrivia_flutter/classes/constants.dart' as constants;
import "package:flutter/services.dart";
import 'package:mutrivia_flutter/classes/user-data.service.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

class HostSessionWaiting extends StatefulWidget {

  const HostSessionWaiting ({Key? key, required this.userId, required this.username,required this.sessionId}) : super(key: key);
  final String userId;
  final String username;
  final String sessionId;

  @override
  _HostSessionWaitingState createState() => _HostSessionWaitingState(userId: userId, username: username, sessionId: sessionId);
}

class _HostSessionWaitingState extends State<HostSessionWaiting> {

  final String userId;
  final String username;
  final String sessionId;

  _HostSessionWaitingState({required this.userId, required this.username, required this.sessionId});
  final channel = WebSocketChannel.connect(
      Uri.parse("${constants.WEB_SOCKET_URL}/topic/newuser")
  );

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
          child: StreamBuilder(
            stream: channel.stream,
            builder: (context, snapshot) {
              print(snapshot);
              return Text(snapshot.hasData ? '${snapshot.data}' : '');
            },
          ),
        ),
      ),
    );
  }
}

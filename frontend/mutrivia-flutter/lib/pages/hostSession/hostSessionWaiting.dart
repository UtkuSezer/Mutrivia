import 'dart:async';
import 'dart:io';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:mutrivia_flutter/classes/constants.dart' as constants;
import "package:flutter/services.dart";
import 'package:mutrivia_flutter/classes/user-data.service.dart';
import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:web_socket_channel/io.dart';
import 'package:mutrivia_flutter/pages/hostSession/HostWebsocketProvider.dart';
import 'package:mutrivia_flutter/models/user.dart';
import 'package:stomp_dart_client/stomp.dart';
import 'package:stomp_dart_client/stomp_config.dart';
import 'package:stomp_dart_client/stomp_frame.dart';
import 'dart:convert';

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

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    getUsersInSession(userId);
  }

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
        child: SafeArea(
          child: Row(
            children: [
              ElevatedButton(
                  onPressed: (){
                    getUsersInSession(userId);
                  },
                  child: const Text("refresh")
              )
            ],
          ),
        ),
        alignment: Alignment.center,
      )
    );
  }

}

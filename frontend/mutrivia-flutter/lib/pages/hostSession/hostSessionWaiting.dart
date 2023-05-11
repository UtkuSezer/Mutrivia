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
    super.initState();
    //final WebsocketProvider websocket = WebsocketProvider(sessionId);
    /*_channel = WebSocketChannel.connect(
      Uri.parse("${constants.WEB_SOCKET_URL}"), //"${constants.WEB_SOCKET_URL}/newuser/$sessionId"
    );*/

    WebSocket ws;
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      run().then((value) {
        setState(() {
          ws = value;
          print("DENIYORUZ $ws");
        });
      });
    });
  }

  Future<WebSocket> run() async {
    Random r = new Random();
    String key = base64.encode(List<int>.generate(8, (_) => r.nextInt(255)));

      HttpClient client = HttpClient();
      HttpClientRequest request = await client.getUrl(
          Uri.parse('http://www.mutrivia.com/api/ws'));
      request.headers.add('connection', 'Upgrade');
      request.headers.add('upgrade', 'websocket');
      request.headers.add('Sec-WebSocket-Version', '13');
      request.headers.add('Sec-WebSocket-Key', key);
      HttpClientResponse response = await request.close();
      Socket socket = await response.detachSocket();

      WebSocket ws = WebSocket.fromUpgradedSocket(socket, serverSide: false);
      ws.listen((event) {
        print(event);
        Map<String, dynamic> res = event;
        print(res.keys.length);
        for(String key in res.keys){
          print(key);
        }
      });
      return ws;
  }

  var _channel;
  WebSocket? ws;

  @override
  Widget build(BuildContext context) {
    var ws;

    ws.listen((event) {
      print(event);
      Map<String, dynamic> res = event;
      print(res.keys.length);
      for(String key in res.keys){
        print(key);
      }
    });

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
              StreamBuilder( //StreamBuilder<User>
                stream: ws?.asBroadcastStream(),
                builder: (context, snapshot) {
                  return Text(snapshot.hasData ? '${snapshot.data}' : '');
                  /*if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(
                  child: CircularProgressIndicator(),
                );
              }

              if (snapshot.connectionState == ConnectionState.active &&
                  snapshot.hasData) {
                return Center(
                  child: Text(
                    '${snapshot.data!.username}: ${snapshot.data!.sessionId}',
                    style: TextStyle(
                      color: Colors.orange,
                      fontSize: 24.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                );
              }

              if (snapshot.connectionState == ConnectionState.done) {
                return const Center(
                  child: Text('No more data'),
                );
              }

              return const Center(
                child: Text('No data'),
              );*/

                },
              ),
              ElevatedButton(
                onPressed: () {
                  _channel.sink.add('Dummy');
                },
                child: const Text('Dummy'),
              ),
            ],
          ),
        ),
        alignment: Alignment.center,
      )
    );
  }

  @override
  void dispose() {
    //channel.sink.close();
    super.dispose();
  }
}

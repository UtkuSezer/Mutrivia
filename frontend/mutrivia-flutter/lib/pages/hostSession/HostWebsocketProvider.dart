import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:mutrivia_flutter/models/user.dart';
import 'dart:convert';
import 'package:mutrivia_flutter/classes/constants.dart' as constants;

class WebsocketProvider {
  late final WebSocketChannel websocket;

  WebsocketProvider(String sessionId)
      : websocket = WebSocketChannel.connect(
    Uri.parse("${constants.WEB_SOCKET_URL}/newuser/$sessionId"),
  );

  Stream<User> get userStream =>
      websocket.stream
          .map<User>(
              (value) => User.fromJson(jsonDecode(value)));

  void closeStream() {
    websocket.sink.close();
  }
}
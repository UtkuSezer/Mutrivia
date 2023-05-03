import 'dart:convert';
import 'package:http/http.dart' as http;
import '../classes/constants.dart' as constants;
import 'package:mutrivia_flutter/models/user.dart';

User parseUser(String responseBody) {
  final parsed = jsonDecode(responseBody);
  return User.fromJson(parsed);
}

Future<User> addUser(String username) async {
  final url = Uri.parse('${constants.USER_API_URL}/add/$username');
  final response = await http.post(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${json.decode(response.body).runtimeType}');
  return parseUser(response.body);
}

Future<void> getUsersInSession(String userId) async {
  final url = Uri.parse('${constants.USER_API_URL}/session/$userId');
  final response = await http.put(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> getUser(String userId) async {
  final url = Uri.parse('${constants.USER_API_URL}/get/$userId');
  final response = await http.get(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> deleteUser(String userId) async {
  final url = Uri.parse('${constants.USER_API_URL}/delete/$userId');
  final response = await http.delete(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> addPointsToUser(String userId, int points) async {
  final url = Uri.parse('${constants.USER_API_URL}/addpoints/$userId/$points');
  final response = await http.put(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}
import 'package:http/http.dart' as http;
import '../classes/constants.dart' as constants;

Future<void> generateQuestion(String userId) async {
  final url = Uri.parse('${constants.GAME_API_URL}/question/$userId');
  final response = await http.get(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> startSession(String userId) async {
  final url = Uri.parse('${constants.GAME_API_URL}/start/$userId');
  final response = await http.get(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> hostSession(String userId, String museumId) async {
  final url = Uri.parse('${constants.GAME_API_URL}/host/$userId/$museumId');
  final response = await http.put(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> joinSession(String userId, String sessionId) async {
  final url = Uri.parse('${constants.GAME_API_URL}/join/$sessionId/$userId');
  final response = await http.put(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> soloSession(String userId, String museumId) async {
  final url = Uri.parse('${constants.GAME_API_URL}/solo/$userId/$museumId');
  final response = await http.put(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> leaveSession(String userId) async {
  final url = Uri.parse('${constants.GAME_API_URL}/leave/$userId');
  final response = await http.get(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}

Future<void> endSession(String userId) async {
  final url = Uri.parse('${constants.GAME_API_URL}/end/$userId');
  final response = await http.get(url);
  print('Status code: ${response.statusCode}');
  print('Body: ${response.body}');
}
class User {
  final String userId;
  final String username;
  final String sessionId;
  final String state;
  final String? museumId;
  final int score;

  const User({
    required this.userId,
    required this.username,
    required this.sessionId,
    required this.state,
    this.museumId,
    required this.score,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userId: json['userId'] as String,
      username: json['username'] as String,
      sessionId: json['sessionId'] as String,
      state: json['state'] as String,
      museumId: json['museumId'] as String?,
      score: json['score'] as int,
    );
  }
}
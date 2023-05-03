class Session {
  final String sessionId;
  final String hostId;
  final bool isJoinable;

  const Session({
    required this.sessionId,
    required this.hostId,
    required this.isJoinable,
  });

  factory Session.fromJson(Map<String, dynamic> json) {
    return Session(
      sessionId: json['sessionId'] as String,
      hostId: json['hostId'] as String,
      isJoinable: json['isJoinable'] as bool,
    );
  }
}
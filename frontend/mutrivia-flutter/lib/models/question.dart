class Question {
  final String questionStatement;
  final List<String> options;
  final int correctChoiceIndex;
  final String userId;

  const Question({
    required this.questionStatement,
    required this.options,
    required this.correctChoiceIndex,
    required this.userId,
  });

  factory Question.fromJson(Map<String, dynamic> json) {
    return Question(
      questionStatement: json['questionStatement'] as String,
      options: json['options'] as List<String>,
      correctChoiceIndex: json['correctChoiceIndex'] as int,
      userId: json['userId'] as String,
    );
  }
}
export interface Question {
    questionStatement: string,
    options: string[],
    correctChoiceIndex: number,
    userId: string,
}
package interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class stie {
	public static void main(String[] args) {

		Parser parser = new Parser(new Scanner("program1.txt"));
		parser.parseOld();
	}
}

class Scanner {
	private String progText;
	private int curPos = 0;

	Scanner(String fileName) {

		try {
			// 1st way
			byte[] allBytes = Files.readAllBytes(Paths.get("program.txt").toAbsolutePath());
			progText = new String(allBytes);
			System.out.println(progText);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	Token nextToken() {
		if (curPos == progText.length())
			return new EOFToken(null);

		char curChar;
		while (curPos < progText.length() && Character.isWhitespace(progText.charAt(curPos)))
			curPos++;

		if (curPos == progText.length())
			return new EOFToken(null);

		curChar = progText.charAt(curPos);
		curPos++;
		if (Character.isDigit(curChar))
			return new DigitToken(String.valueOf(curChar));
		else if (Character.isAlphabetic(curChar))
			return new IdentifierToken(String.valueOf(curChar));
		else if (curChar == '{')
			return new WhileBegToken(String.valueOf(curChar));
		else if (curChar == '}')
			return new WhileEndToken(String.valueOf(curChar));
		else if (curChar == ';')
			return new SemicolonToken(String.valueOf(curChar));
		else if (curChar == '+' || curChar == '-' || curChar == '*' || curChar == '/' || curChar == '^' || curChar == '%')
			return new OperatorToken(String.valueOf(curChar));
		else if (curChar == '<')
			return new OutputToken(String.valueOf(curChar));
		else if (curChar == '>')
			return new InputToken(String.valueOf(curChar));
		else if (curChar == '=')
			return new EqualToken(String.valueOf(curChar));
		else if (curChar == '?')
			return new ConditionToken(String.valueOf(curChar));
		else if (curChar == ':')
			return new ElseToken(String.valueOf(curChar));

		return new IdentifierToken(String.valueOf(curChar));
		// return new ErrorToken("NotRecognizedToken");
	}
}

class Parser {
	private Scanner scanner;

	Parser(Scanner scanner) {
		this.scanner = scanner;
	}

	void parseOld() {
		Token token = scanner.nextToken();
		while (!token.getType().equals(TokenType.END_OF_FILE)) {
			System.out.printf("token text: %s, token type: %s \n", token.text, token.tokenType.text);
			token = scanner.nextToken();
		}
		System.out.println("Done with the program");
	}

	void parseActual() {

	}
}

class Token {
	protected String text;
	protected TokenType tokenType;

	Token(String text) {
		this.text = text;
	}

	TokenType getType() {
		return tokenType;
	}
}

class EOFToken extends Token {
	EOFToken(String text) {
		super("EOF");
		this.tokenType = tokenType.END_OF_FILE;
	}
}

class EqualToken extends Token {
	public EqualToken(String text) {
		super(text);
		this.tokenType = tokenType.EQUAL;
	}
}

class IdentifierToken extends Token {
	IdentifierToken(String text) {
		super(text);
		this.tokenType = tokenType.IDENTIFIER;
	}
}

class ErrorToken extends Token {
	ErrorToken(String text) {
		super(text);
		this.tokenType = tokenType.NRT_ERROR;
	}
}

class DigitToken extends Token {
	public DigitToken(String text) {
		super(text);
		this.tokenType = tokenType.DIG;
	}
}

class WhileBegToken extends Token {
	public WhileBegToken(String text) {
		super(text);
		this.tokenType = tokenType.WHILE_BEG;
	}
}

class WhileEndToken extends Token {
	public WhileEndToken(String text) {
		super(text);
		this.tokenType = tokenType.WHILE_END;
	}
}

class SemicolonToken extends Token {
	public SemicolonToken(String text) {
		super(text);
		this.tokenType = tokenType.SEMICOLON;
	}
}

class OperatorToken extends Token {
	public OperatorToken(String text) {
		super(text);
		this.tokenType = tokenType.OPERATOR;
	}
}

class OutputToken extends Token {
	public OutputToken(String text) {
		super(text);
		this.tokenType = tokenType.OUTPUT;
	}
}

class InputToken extends Token {
	public InputToken(String text) {
		super(text);
		this.tokenType = tokenType.INPUT;
	}
}

class ConditionToken extends Token {
	public ConditionToken(String text) {
		super(text);
		this.tokenType = tokenType.CONDITION;
	}
}

class ElseToken extends Token {
	public ElseToken(String text) {
		super(text);
		this.tokenType = tokenType.ELSE;
	}
}

enum TokenType {
	IF_BEG("If Begining"), IF_END("If End"), WHILE_BEG("While Begining"), WHILE_END("While End"),
	EQUAL("Equal"), DIG("Digit"), IDENTIFIER("Identifier"), SEMICOLON("Semicolon"), OPERATOR("Operator"),
	OUTPUT("Output"), INPUT("Input"), CONDITION("Condition"), ELSE("Else"), END_OF_FILE, NRT_ERROR;

	String text;

	TokenType() {
		this.text = this.toString();
	}

	TokenType(String text) {
		this.text = text;
	}
}
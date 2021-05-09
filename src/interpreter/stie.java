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
		else if (curChar == '+')
			return new Plus(String.valueOf(curChar));
		else if (curChar == '-')
			return new Minus(String.valueOf(curChar));
		else if (curChar == '*')
			return new Multiply(String.valueOf(curChar));
		else if (curChar == '/')
			return new Divide(String.valueOf(curChar));
		else if (curChar == '%')
			return new Modulo(String.valueOf(curChar));
		else if (curChar == '^')
			return new Exponent(String.valueOf(curChar));
		else if (curChar == '(')
			return new LeftParanthes(String.valueOf(curChar));
		else if (curChar == ')')
			return new RightParanthes(String.valueOf(curChar));
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

		return new NotRecognized(String.valueOf(curChar));
		// return new ErrorToken("NotRecognizedToken");
	}
}

class Parser {
	private Scanner scanner;
	private Token curToken;

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
		curToken = scanner.nextToken();
		while (!curToken.getType().equals(TokenType.END_OF_FILE)) {
			if (curToken.getType().equals(TokenType.PERIOD))
				return;
			S();
			// else if(token.getType().equals(TokenType.))
		}
	}

	void S() {
		if (curToken.getType().equals(TokenType.IF_BEG))
			C();
		else if (curToken.getType().equals(TokenType.WHILE_BEG))
			W();
		else if (curToken.getType().equals(TokenType.IDENTIFIER))
			A();
		else if (curToken.getType().equals(TokenType.OUTPUT))
			O();
		else if (curToken.getType().equals(TokenType.INPUT))
			I();
		else {
			System.out.println("There is a problem inside of the Statement");
		}

	}

	void C() {
		curToken = scanner.nextToken();
		E();
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.QUESTION_MARK)) {
			curToken = scanner.nextToken();
			S();
		} else {
			System.out.println("Syntax error, no '?' in if statement");
		}
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.ELSE)) {
			curToken = scanner.nextToken();
			S();
			curToken = scanner.nextToken();
		}

		if (!curToken.getType().equals(TokenType.IF_END)) {
			System.out.println("Syntax error about if end");
		}
	}

	void A() {
		curToken = scanner.nextToken();
		L();
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.EQUAL)) {
			curToken = scanner.nextToken();
			E();
		} else {
			System.out.println("Syntax error, no '=' in assignment.");
		}
		curToken = scanner.nextToken();
		if (!curToken.getType().equals(TokenType.SEMICOLON)) {
			System.out.println("Missing ';' in assignment");
		}

	}

	void W() {
		curToken = scanner.nextToken();
		E();
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.QUESTION_MARK)) {
			curToken = scanner.nextToken();
			S();
		} else {
			System.out.println("Syntax error, no '?' in while statement");
		}
		curToken = scanner.nextToken();
		if (!curToken.getType().equals(TokenType.WHILE_END)) {
			System.out.println("Syntax error about while end");
		}
	}

	void O() {
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.OUTPUT)) {
			curToken = scanner.nextToken();
			E();
		} else {
			System.out.println("Syntax error no '<' in output");
		}
		curToken = scanner.nextToken();
		if (!curToken.getType().equals(TokenType.SEMICOLON)) {
			System.out.println("Missing ';' in assignment");
		}

	}

	void I() {
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.INPUT)) {
			curToken = scanner.nextToken();
			L();
		} else {
			System.out.println("Syntax error no '>' in output");
		}
		curToken = scanner.nextToken();
		if (!curToken.getType().equals(TokenType.SEMICOLON)) {
			System.out.println("Missing ';' in assignment");
		}
	}

	void E() {
		T();
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.PLUS) || curToken.getType().equals(TokenType.MINUS)) {
			curToken = scanner.nextToken();
			T();
			curToken = scanner.nextToken();
			if (curToken.getType().equals(TokenType.PLUS) || curToken.getType().equals(TokenType.MINUS)) {
				E();
			}
		}
	}

	void T() {
		U();
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.MULTIPLY) || curToken.getType().equals(TokenType.DIVIDE)
				|| curToken.getType().equals(TokenType.MODULO)) {
			curToken = scanner.nextToken();
			U();
			if (curToken.getType().equals(TokenType.MULTIPLY) || curToken.getType().equals(TokenType.DIVIDE)
					|| curToken.getType().equals(TokenType.MODULO)) {
				U();
			}
		}
	}

	void U() {
		F();
		curToken = scanner.nextToken();
		if (curToken.getType().equals(TokenType.EXPONENT)) {
			curToken = scanner.nextToken();
			U();
		}
	}

	void F() {
		if (curToken.getType().equals(TokenType.L_PRNTHS)) {
			curToken = scanner.nextToken();
			E();
			curToken = scanner.nextToken();
			if (!curToken.getType().equals(TokenType.R_PRNTHS)) {
				System.out.println("Syntax error no ')'");
			}
		} else if (curToken.getType().equals(TokenType.DIG)) {
			D();
		} else if (curToken.getType().equals(TokenType.IDENTIFIER)) {
			L();
		} else {
			System.out.println("There is unknown type (class F())");
		}
	}

	void D() {
		if (!curToken.getType().equals(TokenType.DIG)) {
			System.out.println("Not digit");
		}
	}

	void L() {
		if (!curToken.getType().equals(TokenType.IDENTIFIER)) {
			System.out.println("Not Identifier");
		}
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

class NotRecognized extends Token {
	NotRecognized(String text) {
		super(text);
		this.tokenType = tokenType.NRT_ERROR;
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

class Period extends Token {
	public Period(String text) {
		super(text);
		this.tokenType = tokenType.PERIOD;
	}
}

class Plus extends Token {
	public Plus(String text) {
		super(text);
		this.tokenType = tokenType.PLUS;
	}
}

class Minus extends Token {
	public Minus(String text) {
		super(text);
		this.tokenType = tokenType.MINUS;
	}
}

class Multiply extends Token {
	public Multiply(String text) {
		super(text);
		this.tokenType = tokenType.MULTIPLY;
	}
}

class Divide extends Token {
	public Divide(String text) {
		super(text);
		this.tokenType = tokenType.DIVIDE;
	}
}

class Modulo extends Token {
	public Modulo(String text) {
		super(text);
		this.tokenType = tokenType.MODULO;
	}
}

class Exponent extends Token {
	public Exponent(String text) {
		super(text);
		this.tokenType = tokenType.EXPONENT;
	}
}

class LeftParanthes extends Token {
	public LeftParanthes(String text) {
		super(text);
		this.tokenType = tokenType.L_PRNTHS;
	}
}

class RightParanthes extends Token {
	public RightParanthes(String text) {
		super(text);
		this.tokenType = tokenType.R_PRNTHS;
	}
}

enum TokenType {
	IF_BEG("If Begining"), IF_END("If End"), WHILE_BEG("While Begining"), WHILE_END("While End"),
	QUESTION_MARK("Question Mark"), EQUAL("Equal"), DIG("Digit"), IDENTIFIER("Identifier"), SEMICOLON("Semicolon"),
	OUTPUT("Output"), INPUT("Input"), CONDITION("Condition"), ELSE("Else"), END_OF_FILE, PERIOD("Period"), NRT_ERROR,
	PLUS("Plus"), EXPONENT("Exponent"), L_PRNTHS("Left Paranthes"), R_PRNTHS("Right Paranthes"), MINUS("Minus"),
	MULTIPLY("Multiply"), DIVIDE("Divide"), MODULO("Modulo");

	String text;

	TokenType() {
		this.text = this.toString();
	}

	TokenType(String text) {
		this.text = text;
	}
}
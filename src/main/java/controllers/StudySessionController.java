package controllers;

import static studysession.KeyInput.EXIT;
import static studysession.KeyInput.MARKEASY;
import static studysession.KeyInput.MARKHARD;
import static studysession.KeyInput.SEEANSWER;
import static studysession.KeyInput.UNKNOWNINPUT;

import readers.SystemInReader;
import studysession.KeyInput;
import studysession.QuestionBank;
import studysession.QuestionBlock;
import studysession.srFile;
import studysession.Session;
import viewers.StudySessionViewer;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * This class represents the controller for when we want to hold a study session
 */
public class StudySessionController implements Controller {

  private final Session session;
  private final StudySessionViewer view;
  private final Readable input;
  private final Appendable output;

  /**
   * Default constructor
   *
   * @param input The input this controller will take in
   * @param output The output this reader will give out to its viewer
   */
  public StudySessionController(Readable input, Appendable output, Session session) {
    this.input = Objects.requireNonNull(input);
    this.output = Objects.requireNonNull(output);
    this.view = new StudySessionViewer(output);
    this.session = session;
  }

  /**
   * Runs our program
   */
  @Override
  public void run() throws IOException {

    SystemInReader reader = new SystemInReader(input);
    //prompt user, accept input and turn it into path for sr file
    this.view.messageDisplay("Study session mode initialized. Please input the desired file path"
        + " for the SR file to study from");
    String desiredsr = reader.read();
    Path srReference = Path.of(desiredsr);
    srFile srFile = new srFile(srReference);

    //prompt for number of questions they want to study
    this.view.messageDisplay("How many questions would you like to study?");
    int questionAmount;
    try {
      questionAmount = Integer.parseInt(reader.read());
    } catch (NumberFormatException e) {
      this.view.messageDisplay("Not a number!");
      throw new IllegalArgumentException("Input must be a number");
    }
    //accept that number, gen that num of random questions from given file
    Random rand = new Random();
    QuestionBank bank = srFile.generateQuestions();
    ArrayList<QuestionBlock> rawBank =
        this.session.generateRandomQuestions(bank, questionAmount, rand);
    int i = 0;
    int amountOfQuestionsToIterate = Math.min(questionAmount, rawBank.size());
    while (i < amountOfQuestionsToIterate) {
      QuestionBlock qb = rawBank.get(i);
      this.view.messageDisplay(qb.getQuestion());
      this.view.messageDisplay("Press 1 to mark a question as easy, 2 as hard, 3 to see the answer"
          + " and 0 to exit. ");
      String input = reader.read();
      KeyInput key = this.inputProcessing(input);
      i += this.keyInputProcessing(key, qb);
    }
    for (String s : this.session.getStats(rawBank)) {
      this.view.messageDisplay(s);
    }
    bank = new QuestionBank(rawBank);
    FileWriter fw = new FileWriter(srReference.toFile());
    fw.write(this.getNewInformation(bank));
    fw.close();

  }

  /**
   * Given a certain string input, return the corresponding enumeration
   *
   * @param input the input we want to process
   * @return The corresponding KeyInput enumeration
   */
  public KeyInput inputProcessing(String input) {
    KeyInput output;
    switch (input) {
      case "1":
        output = MARKEASY;
        break;
      case "2":
        output = MARKHARD;
        break;
      case "3":
        output = SEEANSWER;
        break;
      case "0":
        output = EXIT;
        break;
      default:
        output = UNKNOWNINPUT;
    }

    return output;
  }

  /**
   * Calls the corresponding method for a given KeyInput
   *
   * @param key the KeyInput to process
   * @param qb the qb we may modify
   * @return an int representing whether to advance forward in our question queue
   * @throws IOException When something goes wrong
   */
  public int keyInputProcessing(KeyInput key, QuestionBlock qb) throws IOException {
    switch (key) {
      case MARKEASY:
        if (qb.getDifficulty() >= 1) {
          this.session.incrementToEasyQuestions();
        }
        this.session.incrementQuestionsAnswered();
        qb.changeDifficulty(0);
        return 1;
      case MARKHARD:
        if (qb.getDifficulty() < 1) {
          this.session.incrementToHardQuestions();
        }
        this.session.incrementQuestionsAnswered();
        qb.changeDifficulty(1);
        return 1;
      case SEEANSWER:
        this.view.messageDisplay(qb.getAnswer());
        return 0;
      case EXIT:
        return 1000000;
      case UNKNOWNINPUT:
        this.view.messageDisplay("Sorry, try another input");
        return 0;
      default:
        return 0;
    }

  }

  public String getNewInformation(QuestionBank bank) {
    return bank.toString();
  }






}

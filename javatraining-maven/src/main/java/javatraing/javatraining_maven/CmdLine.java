package javatraing.javatraining_maven;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdLine implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(CmdLine.class
			.getName());

	private String[] args;

	private Options options = new Options();

	private final static XmlFile xmlFile = XmlFile.getNewInstance();

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public CmdLine() {
		options.addOption("a", "add", false, "add one new person message .");
		options.addOption("d", "delete", false, "delete one person message");
		options.addOption("s", "search", false,
				"search the specific person message");
		options.addOption(OptionBuilder.withValueSeparator().hasArgs()
				.withDescription("user name").create('u'));
		options.addOption(OptionBuilder.withValueSeparator().hasArgs()
				.withDescription("user id").create('i'));
		options.addOption(OptionBuilder.withValueSeparator().hasArgs()
				.withDescription("user address").create('r'));
		options.addOption(OptionBuilder.withValueSeparator().hasArgs()
				.withDescription("user phoneNumber").create('p'));
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	private void printUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("addressbook", options);
	}

	public void run() {
		try {
			CommandLineParser parser = new PosixParser();
			CommandLine line = parser.parse(options, args);
			PeopleMsg peopMsg = new PeopleMsg();
			if (line.hasOption("a")) {
				if (line.hasOption("u") && line.hasOption("i")
						&& line.hasOption("r") && line.hasOption("p")) {
					peopMsg = new PeopleMsg();
					peopMsg.setName(line.getOptionValue("u"));
					peopMsg.setId(line.getOptionValue("i"));
					peopMsg.setAddress(line.getOptionValue("r"));
					peopMsg.setPhoneNum(line.getOptionValue("p"));
					xmlFile.addNode(peopMsg);
					System.out.println("Create user successfully. "
							+ peopMsg.toString());
					logger.info("Create new user: " + peopMsg.toString());
				} else {
					printUsage();
					logger.info("Failed to create new user,arguments are insuffient. ");
				}
			} else if (line.hasOption("s")) {
				if (line.hasOption("u")) {
					peopMsg.setName(line.getOptionValue("u"));
					peopMsg = xmlFile.getPeopMsg(peopMsg);
					System.out.println("Get user message: "
							+ peopMsg.toString());
				} else if (line.hasOption("r")) {
					peopMsg.setAddress(line.getOptionValue("r"));
					peopMsg = xmlFile.getPeopMsg(peopMsg);
					System.out.println("Get user message: "
							+ peopMsg.toString());
				} else if (line.hasOption("p")) {
					peopMsg.setPhoneNum(line.getOptionValue("p"));
					peopMsg = xmlFile.getPeopMsg(peopMsg);
					System.out.println("Get user message: "
							+ peopMsg.toString());
				} else {
					printUsage();
					logger.info("Failed to get user,arguments are insuffient. ");
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		CmdLine cmdLine = new CmdLine();
		cmdLine.setArgs(args);
		executorService.execute(cmdLine);
		executorService.shutdown();
	}

}

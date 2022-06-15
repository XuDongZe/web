package com.xdz.tinygit.cmdparser;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.apachecommons.CommonsLog;
import picocli.CommandLine;

/**
 * Description: parse java cmd string, using picocli lib<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/15 18:45<br/>
 * Version: 1.0<br/>
 *
 * <a href="https://blog.csdn.net/jimo_lonely/article/details/119616193">picocli using</a></br>
 * <a href="https://github.com/kakawait/picocli-spring-boot-starter">picocli github</a></br>
 */
@CommonsLog
public class CmdParser {
    @CommandLine.Command(
            name = "java -jar app.jar",
            subcommands = {ExportCmd.class, ImportCmd.class},
            mixinStandardHelpOptions = true,
            helpCommand = true)
    public static class CmdParam {
    }

    @CommandLine.Command(name = "export", mixinStandardHelpOptions = true, helpCommand = true)
    public static class ExportCmd {
        @CommandLine.Option(names = {"-r", "--jdbc-url"}, required = true, description = "数据库地址")
        public String jdbcUrl;
        @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "用户名")
        public String username;
        @CommandLine.Option(names = {"-p", "--secret-key"}, required = true, description = "秘钥")
        public String secretKey;
        @CommandLine.Option(names = {"-d", "--database"}, defaultValue = "default", description = "库名，默认为default")
        public String database;
        @CommandLine.Option(names = {"-t", "--table"}, description = "表名，多个表用逗号分开")
        public String tables;
    }

    @CommandLine.Command(name = "import", mixinStandardHelpOptions = true, helpCommand = true)
    public static class ImportCmd {
        @CommandLine.Option(names = {"-r", "--jdbc-url"}, required = true, description = "数据库地址")
        public String jdbcUrl;
        @CommandLine.Option(names = {"-u", "--username"}, required = true, description = "用户名")
        public String username;
        @CommandLine.Option(names = {"-p", "--secret-key"}, required = true, description = "秘钥")
        public String secretKey;
        @CommandLine.Option(names = {"-f", "--from"}, required = true, description = "来源")
        public String from;
        @CommandLine.Option(names = {"-t", "--to"}, required = true, description = "目标")
        public String to;
    }


    public static void main(String[] args) {
        CmdParam cmdParam = new CmdParam();
        final CommandLine commandLine = new CommandLine(cmdParam);
        try {
            final CommandLine.ParseResult parseResult = commandLine.parseArgs(args);
            checkParamHelp(args.length == 0, commandLine, parseResult);
            if (parseResult.hasSubcommand()) {
                for (CommandLine.ParseResult subCommand : parseResult.subcommands()) {
                    final CommandLine c = subCommand.commandSpec().commandLine();
                    checkParamHelp(args.length == 1, c, subCommand);
                }
            }
        } catch (CommandLine.ParameterException e) {
            commandLine.usage(System.out);
            for (CommandLine c : commandLine.getSubcommands().values()) {
                c.usage(System.out);
            }
            System.exit(1);
        }
        log.info("cmd params:{" + JSONObject.toJSONString(cmdParam) + "}");
    }

    private static void checkParamHelp(boolean empty, CommandLine commandLine, CommandLine.ParseResult parseResult) {
        if (empty || parseResult.isUsageHelpRequested()) {
            commandLine.usage(System.out);
        }
        if (parseResult.isVersionHelpRequested()) {
            commandLine.printVersionHelp(System.out);
        }
    }
}

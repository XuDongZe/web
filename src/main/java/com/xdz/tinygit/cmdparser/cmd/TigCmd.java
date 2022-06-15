package com.xdz.tinygit.cmdparser.cmd;

import com.xdz.tinygit.cmdparser.CmdParser;
import picocli.CommandLine;

/**
 * Description: tig common<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/15 18:59<br/>
 * Version: 1.0<br/>
 */
@CommandLine.Command(
        name = "java -jar tig.jar",
        subcommands = {CmdParser.ExportCmd.class, CmdParser.ImportCmd.class},
        mixinStandardHelpOptions = true,
        helpCommand = true)
public class TigCmd {
}

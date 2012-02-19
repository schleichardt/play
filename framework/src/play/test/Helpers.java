package play.test;

import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.exceptions.TemplateCompilationException;
import play.templates.Template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    static Pattern pattern = Pattern.compile("^(\\w+)\\(\\s*(?:('(?:\\\\'|[^'])*'|[^.]+?)\\s*(?:,\\s*('(?:\\\\'|[^'])*'|[^.]+?)\\s*)?)?\\)$");

    public static String[] seleniumCommand(String command) {
        return seleniumCommand(command, null, null);
    }

    public static String[] seleniumCommand(String command, Template template, Integer lineNumber) {
        Matcher matcher = pattern.matcher(command.trim());
        if (matcher.matches()) {
            String[] result = new String[3];
            result[0] = matcher.group(1);
            result[1] = matcher.group(2)!=null?matcher.group(2):"";
            result[2] = matcher.group(3)!=null?matcher.group(3):"";
            for (int i = 0; i < result.length; i++) {
                if (result[i].matches("^'.*'$")) {
                    result[i] = result[i].substring(1, result[i].length() - 1);
                    result[i] = result[i].replace("\\'", "'");
                }
            }
            return result;
        } else {
            if (StringUtils.isNotBlank(command) && template != null && lineNumber != null) {
                //TODO test if it works with nested templates
                //TODO ant test
                //TODO write tests, for example with https://play.lighthouseapp.com/projects/57987/tickets/695-selenium-tests-being-silently-ignored => verifyElementPresent('//ul[@id='home']/li/[a.='Login']') should not compile
                Logger.error("Can't parse Selenium command (" + command + ")");
                throw new TemplateCompilationException(template, lineNumber, "invalid Selenium syntax");
            }
            return null;
        }
    }
}

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.io.Resources;

public class GrammarUtils {
	public static boolean convergent(URL grammarA, URL grammarB) throws IOException {
		String linesGrammarA = Resources.toString(grammarA, Charsets.UTF_8);
		String linesGrammarB = Resources.toString(grammarB, Charsets.UTF_8);

		List<Rule> rulesA = Rule.getRules(linesGrammarA);

		List<Rule> rulesB = Rule.getRules(linesGrammarB);
		
		SetView<Rule> intersection = Sets.intersection(Sets.newHashSet(rulesA), Sets.newHashSet(rulesB));
		System.out.println(intersection);

		return !intersection.isEmpty();
	}

	private static class Rule {
		private static List<Rule> getRules(String grammar) {
			List<Rule> rules = new ArrayList<>();

			Pattern pattern = Pattern.compile("\\n\\w+( |\\n|\\t)*:");
			Matcher matcher = pattern.matcher(grammar);

			while (matcher.find()) {
				String rule = matcher.group().replaceAll("\\n", "").replaceAll(":", "").trim();
				rules.add(new Rule(rule));
//				System.out.println(rule);
			}

			return rules;
		}

		private String rule;

		public Rule(String rule) {
			this.rule = rule;
		}

		public String getRule() {
			return rule;
		}

		@Override
		public String toString() {
			return rule;
		}
		
		@Override
		public int hashCode() {
			return getRule().toLowerCase().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			Rule b = (Rule) obj;

			return rule.toLowerCase().equals(b.getRule().toLowerCase());
		}

	}

	public static void main(String[] args) throws IOException {
		GrammarUtils.convergent(Resources.getResource("XPath.g4"), Resources.getResource("WCPSLexerTokens.g4"));
	}
}

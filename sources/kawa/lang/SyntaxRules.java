package kawa.lang;

import gnu.expr.Compilation;
import gnu.expr.ErrorExp;
import gnu.expr.ScopeExp;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Procedure1;
import gnu.text.Printable;
import gnu.text.ReportFormat;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SyntaxRules extends Procedure1 implements Printable, Externalizable {
    Object[] literal_identifiers;
    int maxVars = 0;
    SyntaxRule[] rules;

    public SyntaxRules(Object[] literal_identifiers, SyntaxRule[] rules, int maxVars) {
        this.literal_identifiers = literal_identifiers;
        this.rules = rules;
        this.maxVars = maxVars;
    }

    public SyntaxRules(Object[] literal_identifiers, Object srules, Translator tr) {
        this.literal_identifiers = literal_identifiers;
        int rules_count = Translator.listLength(srules);
        if (rules_count < 0) {
            rules_count = 0;
            tr.syntaxError("missing or invalid syntax-rules");
        }
        this.rules = new SyntaxRule[rules_count];
        SyntaxForm rules_syntax = null;
        int i = 0;
        while (i < rules_count) {
            SyntaxForm srules2;
            while (srules2 instanceof SyntaxForm) {
                rules_syntax = srules2;
                srules2 = rules_syntax.getDatum();
            }
            Pair rules_pair = (Pair) srules2;
            SyntaxForm rule_syntax = rules_syntax;
            SyntaxForm syntax_rule = rules_pair.getCar();
            while (syntax_rule instanceof SyntaxForm) {
                rule_syntax = syntax_rule;
                syntax_rule = rule_syntax.getDatum();
            }
            if (syntax_rule instanceof Pair) {
                SyntaxForm pattern_syntax = rule_syntax;
                Pair syntax_rule_pair = (Pair) syntax_rule;
                Object pattern = syntax_rule_pair.getCar();
                String save_filename = tr.getFileName();
                int save_line = tr.getLineNumber();
                int save_column = tr.getColumnNumber();
                SyntaxForm template_syntax = rule_syntax;
                tr.setLine((Object) syntax_rule_pair);
                Object syntax_rule2 = syntax_rule_pair.getCdr();
                while (syntax_rule2 instanceof SyntaxForm) {
                    template_syntax = (SyntaxForm) syntax_rule2;
                    syntax_rule2 = template_syntax.getDatum();
                }
                if (syntax_rule2 instanceof Pair) {
                    syntax_rule_pair = (Pair) syntax_rule2;
                    if (syntax_rule_pair.getCdr() != LList.Empty) {
                        tr.syntaxError("junk after " + i + "'th syntax rule");
                        tr.setLine(save_filename, save_line, save_column);
                        return;
                    }
                    try {
                        Object template = syntax_rule_pair.getCar();
                        tr.push((ScopeExp) PatternScope.push(tr));
                        while (pattern instanceof SyntaxForm) {
                            pattern_syntax = (SyntaxForm) pattern;
                            pattern = pattern_syntax.getDatum();
                        }
                        StringBuffer programbuf = new StringBuffer();
                        if (pattern instanceof Pair) {
                            literal_identifiers[0] = ((Pair) pattern).getCar();
                            Pair p = (Pair) pattern;
                            programbuf.append('\f');
                            programbuf.append('\u0018');
                            this.rules[i] = new SyntaxRule(new SyntaxPattern(programbuf, p.getCdr(), pattern_syntax, literal_identifiers, tr), template, template_syntax, tr);
                            PatternScope.pop(tr);
                            tr.pop();
                            tr.setLine(save_filename, save_line, save_column);
                            i++;
                            srules2 = rules_pair.getCdr();
                        } else {
                            tr.syntaxError("pattern does not start with name");
                            tr.setLine(save_filename, save_line, save_column);
                            return;
                        }
                    } catch (Throwable th) {
                        tr.setLine(save_filename, save_line, save_column);
                        throw th;
                    }
                }
                tr.syntaxError("missing template in " + i + "'th syntax rule");
                tr.setLine(save_filename, save_line, save_column);
                return;
            }
            tr.syntaxError("missing pattern in " + i + "'th syntax rule");
            return;
        }
        i = this.rules.length;
        while (true) {
            i--;
            if (i >= 0) {
                int size = this.rules[i].patternNesting.length();
                if (size > this.maxVars) {
                    this.maxVars = size;
                }
            } else {
                return;
            }
        }
    }

    public Object apply1(Object arg) {
        if (!(arg instanceof SyntaxForm)) {
            return expand(arg, (Translator) Compilation.getCurrent());
        }
        SyntaxForm sf = (SyntaxForm) arg;
        Translator tr = (Translator) Compilation.getCurrent();
        ScopeExp save_scope = tr.currentScope();
        tr.setCurrentScope(sf.getScope());
        try {
            Object expand = expand(sf, tr);
            return expand;
        } finally {
            tr.setCurrentScope(save_scope);
        }
    }

    public Object expand(Object obj, Translator tr) {
        Object[] vars = new Object[this.maxVars];
        Macro macro = (Macro) tr.getCurrentSyntax();
        for (SyntaxRule rule : this.rules) {
            if (rule == null) {
                return new ErrorExp("error defining " + macro);
            }
            if (rule.pattern.match(obj, vars, 0)) {
                return rule.execute(vars, tr, TemplateScope.make(tr));
            }
        }
        return tr.syntaxError("no matching syntax-rule for " + this.literal_identifiers[0]);
    }

    public void print(Consumer out) {
        out.write("#<macro ");
        ReportFormat.print(this.literal_identifiers[0], out);
        out.write(62);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.literal_identifiers);
        out.writeObject(this.rules);
        out.writeInt(this.maxVars);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.literal_identifiers = (Object[]) in.readObject();
        this.rules = (SyntaxRule[]) in.readObject();
        this.maxVars = in.readInt();
    }
}

package kawa.standard;

import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.FluidLetExp;
import gnu.expr.ReferenceExp;
import gnu.expr.ScopeExp;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Symbol;
import kawa.lang.Syntax;
import kawa.lang.SyntaxForm;
import kawa.lang.Translator;

public class fluid_let extends Syntax {
    public static final fluid_let fluid_let = new fluid_let();
    Expression defaultInit;
    boolean star;

    static {
        fluid_let.setName("fluid-set");
    }

    public fluid_let(boolean star, Expression defaultInit) {
        this.star = star;
        this.defaultInit = defaultInit;
    }

    public fluid_let() {
        this.star = false;
    }

    public Expression rewrite(Object obj, Translator tr) {
        if (!(obj instanceof Pair)) {
            return tr.syntaxError("missing let arguments");
        }
        Pair pair = (Pair) obj;
        return rewrite(pair.getCar(), pair.getCdr(), tr);
    }

    public Expression rewrite(Object bindings, Object body, Translator tr) {
        int decl_count = this.star ? 1 : LList.length(bindings);
        Expression[] inits = new Expression[decl_count];
        Expression let = new FluidLetExp(inits);
        int i = 0;
        while (i < decl_count) {
            LList bindings2;
            Pair bind_pair = (Pair) bindings2;
            Object savePos = tr.pushPositionOf(bind_pair);
            try {
                Expression rewrite;
                Object name = bind_pair.getCar();
                if (!(name instanceof String) && !(name instanceof Symbol)) {
                    if (name instanceof Pair) {
                        Pair binding = (Pair) name;
                        if ((binding.getCar() instanceof String) || (binding.getCar() instanceof Symbol) || (binding.getCar() instanceof SyntaxForm)) {
                            name = binding.getCar();
                            if (name instanceof SyntaxForm) {
                                name = ((SyntaxForm) name).getDatum();
                            }
                            if (binding.getCdr() != LList.Empty) {
                                if (binding.getCdr() instanceof Pair) {
                                    binding = (Pair) binding.getCdr();
                                    if (binding.getCdr() == LList.Empty) {
                                        rewrite = tr.rewrite(binding.getCar());
                                    }
                                }
                                let = tr.syntaxError("bad syntax for value of " + name + " in " + getName());
                                tr.popPositionOf(savePos);
                                break;
                            }
                            rewrite = this.defaultInit;
                        }
                    }
                    let = tr.syntaxError("invalid " + getName() + " syntax");
                    tr.popPositionOf(savePos);
                    break;
                }
                rewrite = this.defaultInit;
                Declaration decl = let.addDeclaration(name);
                Declaration found = tr.lexical.lookup(name, false);
                if (found != null) {
                    found.maybeIndirectBinding(tr);
                    decl.base = found;
                    found.setFluid(true);
                    found.setCanWrite(true);
                }
                decl.setCanWrite(true);
                decl.setFluid(true);
                decl.setIndirectBinding(true);
                if (rewrite == null) {
                    rewrite = new ReferenceExp(name);
                }
                inits[i] = rewrite;
                decl.noteValue(null);
                bindings2 = bind_pair.getCdr();
                tr.popPositionOf(savePos);
                i++;
            } catch (Throwable th) {
                tr.popPositionOf(savePos);
                throw th;
            }
        }
        tr.push((ScopeExp) let);
        if (!this.star || bindings2 == LList.Empty) {
            let.body = tr.rewrite_body(body);
        } else {
            let.body = rewrite(bindings2, body, tr);
        }
        tr.pop(let);
        return let;
    }
}

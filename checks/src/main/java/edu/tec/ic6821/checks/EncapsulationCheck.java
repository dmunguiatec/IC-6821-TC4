package edu.tec.ic6821.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EncapsulationCheck extends AbstractCheck {

    private static final String MSG_GETTER_NOT_ALLOWED = "[IC-6821] El uso del getter %s rompe el principio de encapsulamiento";
    private static final String MSG_SETTER_NOT_ALLOWED = "[IC-6821] El uso del setter %s rompe el principio de encapsulamiento";

    private final Set<String> ignore = new HashSet<>();
    private boolean allowGetters = false;
    private boolean allowSetters = false;

    public void setIgnore(String... ignore) {
        this.ignore.addAll(Arrays.asList(ignore));
    }

    public void setAllowGetters(boolean allowGetters) {
        this.allowGetters = allowGetters;
    }

    public void setAllowSetters(boolean allowSetters) {
        this.allowSetters = allowSetters;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.CLASS_DEF};
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast) {
        super.visitToken(ast);

        final DetailAST ident = ast.findFirstToken(TokenTypes.IDENT);
        final List<String> matches = this.ignore.stream()
            .filter(name -> ident.getText().matches(name))
            .collect(Collectors.toList());

        if (!matches.isEmpty()) {
            return;
        }

        final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
        final List<String> fields = getFields(objBlock);
        final List<DetailAST> methodDefs = new ASTWalker(objBlock).filterTokensByType(TokenTypes.METHOD_DEF);

        if (!this.allowGetters) {
            final List<DetailAST> getters = findGetters(fields, methodDefs);
            getters.forEach(getter -> log(getter.getLineNo(),
                String.format(MSG_GETTER_NOT_ALLOWED, getter.getText())));
        }

        if (!this.allowSetters) {
            final List<DetailAST> setters = findSetters(fields, methodDefs);
            setters.forEach(setter -> log(setter.getLineNo(),
                String.format(MSG_SETTER_NOT_ALLOWED, setter.getText())));
        }
    }

    private List<String> getFields(DetailAST objBlock) {
        final List<DetailAST> variableDefs = new ASTWalker(objBlock).filterTokensByType(TokenTypes.VARIABLE_DEF);
        return variableDefs.stream()
            .map(variableDef -> variableDef.findFirstToken(TokenTypes.IDENT))
            .filter(Objects::nonNull)
            .map(DetailAST::getText)
            .collect(Collectors.toList());
    }

    private List<DetailAST> findGetters(List<String> fields, List<DetailAST> methodDefs) {
        final List<String> getters = fields.stream()
            .map(field -> String.format("get%s", StringUtils.capitalize(field)))
            .collect(Collectors.toList());

        return methodDefs.stream()
            .map(methodDef -> methodDef.findFirstToken(TokenTypes.IDENT))
            .filter(Objects::nonNull)
            .filter(ident -> getters.contains(ident.getText()))
            .collect(Collectors.toList());
    }

    private List<DetailAST> findSetters(List<String> fields, List<DetailAST> methodDefs) {
        final List<String> setters = fields.stream()
            .map(field -> String.format("set%s", StringUtils.capitalize(field)))
            .collect(Collectors.toList());

        return methodDefs.stream()
            .map(methodDef -> methodDef.findFirstToken(TokenTypes.IDENT))
            .filter(Objects::nonNull)
            .filter(ident -> setters.contains(ident.getText()))
            .collect(Collectors.toList());
    }

}

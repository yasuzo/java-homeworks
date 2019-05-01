package hr.fer.zemris.java.hw06.shell.commands.massrename_util;

import com.sun.jdi.ObjectReference;

import javax.naming.Name;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parser for parsing a renaming pattern used by {@link hr.fer.zemris.java.hw06.shell.commands.MassrenameCommand}.
 *
 * @author Jan Capek
 */
public class NameBuilderParser {

    private NameBuilder nameBuilder;

    /**
     * Returns a new {@link NameBuilder} that builds a name with given string.
     *
     * @param s String that should be added to a name.
     * @return New name builder.
     * @throws NullPointerException If given string is {@code null}.
     */
    private static NameBuilder text(String s) {
        Objects.requireNonNull(s);
        return (result, sb) -> sb.append(s);
    }

    /**
     * Returns a new  {@link NameBuilder} that appends a group at given index to a name.
     *
     * @param index Index of a group.
     * @return New name builder.
     */
    private static NameBuilder group(int index) {
        return (result, sb) -> sb.append(result.group(index));
    }

    /**
     * Returns a new  {@link NameBuilder} that appends a group at given index to a name and adds padding if necessary.
     *
     * @param index Index of a group.
     * @param padding Character used for padding.
     * @param minWidth Minimum length of a string that should be appended.
     * @return New name builder.
     */
    private static NameBuilder group(int index, char padding, int minWidth) {
        return (result, sb) -> {
            String group = result.group(index);
            for (int i = group.length(); i < minWidth; i++) {
                sb.append(padding);
            }
            sb.append(group);
        };
    }

    /**
     * Creates a new parser with given expression.
     *
     * @param expression Expression used for renaming.
     * @throws NullPointerException If given expression is {@code null}.
     * @throws IllegalArgumentException If given expression is invalid.
     */
    public NameBuilderParser(String expression) {
        Objects.requireNonNull(expression);

//        normal text
        String[] normalText = expression.split("\\$\\{(.*?)}");

//        create group name builders
        List<NameBuilder> groupNameBuilders = Pattern.compile("\\$\\{(.*?)}")
                .matcher(expression)
                .results()
                .map(matchResult -> matchResult.group(1))
                .map(this::createGroupNameBuilder)
                .collect(Collectors.toList());

        Iterator<NameBuilder> it = groupNameBuilders.iterator();

//        create a composite nameBuilder
        NameBuilder nameBuilder = text("");
        for(String s : normalText) {
            if(s.contains("${")) {
                throw new IllegalArgumentException("Unclosed group tag.");
            }
            nameBuilder = nameBuilder.then(text(s));
            if(it.hasNext()) {
                nameBuilder = nameBuilder.then(it.next());
            }
        }

        this.nameBuilder = nameBuilder;
    }

    /**
     * @return Main name builder.
     */
    public NameBuilder getNameBuilder() {
        return nameBuilder;
    }

    /**
     * Creates a name builder for a group.
     *
     * @param group String representation of a grouping syntax.
     * @return New name builder.
     */
    private NameBuilder createGroupNameBuilder(String group) {
        String[] parts = group.split(",");
        if(parts.length != 1 && parts.length != 2) {
            throw new IllegalArgumentException("Invalid group syntax in the expression.");
        }

        String a = parts[0].trim();
        String b = parts.length == 2 ? parts[1].trim() : "0";
        char padding = b.startsWith("0") ? '0' : ' ';

//        parse group index and min width
        int groupIndex, minWidth;
        try {
            groupIndex = Integer.parseInt(a);
            minWidth = Integer.parseInt(b);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid grouping, only numbers should be in a group.");
        }

//        check if min width is less than 0, group index cannot be checked here.
        if(minWidth < 0) {
            throw new IllegalArgumentException("Minimal width cannot be less than 0.");
        }

        return group(groupIndex, padding, minWidth);
    }


}

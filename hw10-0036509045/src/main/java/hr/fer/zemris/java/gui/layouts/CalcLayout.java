package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class CalcLayout implements LayoutManager2 {

    private static final int ROW_COUNT = 5;
    private static final int COLUMN_COUNT = 7;

    /**
     * Map of components and their positions in the grid.
     */
    private Map<RCPosition, Component> components;

    /**
     * Size of a gap between components.
     */
    private int componentGap;

    /**
     * Flag that indicates if placement parameters need to be updated.
     */
    private boolean placementParamsNeedUpdate;

    /**
     * Origin of a container. This is used in case the container has borders
     * in which case we don't want to place content over the borders.
     */
    private Point containerOrigin;

    /**
     * Width of one cell in the grid.
     */
    private int itemWidth;

    /**
     * Height of one cell in the grid.
     */
    private int itemHeight;

    /**
     * Value that needs to be added to {@link this#itemWidth} every few columns
     * in order to stretch the grid to the end of the container.
     */
    private int deltaX;

    /**
     * Value that needs to be added to {@link this#itemHeight} every few rows
     * in order to stretch the grid to the end of the container.
     */
    private int deltaY;

    /**
     * This says how often {@link this#deltaX} should be added to {@link this#itemWidth}.
     * E.g. if this value is 4 that means every 4th column deltaX will be added to itemWidth.
     */
    private int extraPixelDistWidth;

    /**
     * This says how often {@link this#deltaY} should be added to {@link this#itemHeight}.
     * E.g. if this value is 4 that means every 4th row deltaY will be added to itemHeight.
     */
    private int extraPixelDistHeight;

    /**
     * Constructs a new CalcLayout.
     *
     * @param componentGap Gap between components expressed in pixels.
     * @throws IllegalArgumentException If {@code componentGap < 0}.
     */
    public CalcLayout(int componentGap) {
        if (componentGap < 0) {
            throw new IllegalArgumentException("Gap between components cannot be less than 0.");
        }
        this.componentGap = componentGap;
        placementParamsNeedUpdate = true;
        components = new HashMap<>();
    }

    /**
     * Constructs a new CalcLayout with gap between components of 0.
     * It is the same as calling {@code new CalcLayout(0)}.
     *
     * @see CalcLayout#CalcLayout(int)
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * Returns instance of {@link RCPosition} from object.
     * Given parameter can be a string (in which case it has to represent an RCPosition, see {@link RCPosition#fromString(String)})
     * or already an instance of RCPosition.
     *
     * @param rcPosition Representation of RCPosition.
     * @return RCPosition gotten from given object.
     * @throws NullPointerException     If given object is {@code null}.
     * @throws IllegalArgumentException If given object is not string or RCPosition
     *                                  or it is a string but does not represent an RCPosition.
     */
    private static RCPosition getRCPosition(Object rcPosition) {
        Objects.requireNonNull(rcPosition);
        if (rcPosition instanceof String == false && rcPosition instanceof RCPosition == false) {
            throw new IllegalArgumentException("Constraint is not and does not represent instance of RCPosition.");
        }
        return rcPosition instanceof String ? RCPosition.fromString((String) rcPosition) : (RCPosition) rcPosition;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException     If any of the parameters are {@code null}.
     * @throws IllegalArgumentException If given constraint is not a string or an RCPosition or it is a string
     *                                  but it does not represent an RCPosition.
     * @throws CalcLayoutException      If given position is not valid or the position is not empty.
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        Objects.requireNonNull(comp);

        RCPosition position = getRCPosition(constraints);
        if (components.containsKey(position)) {
            throw new CalcLayoutException("Position is not empty.");
        }
        if (position.row < 1 || position.row > ROW_COUNT || position.column < 1 || position.column > COLUMN_COUNT) {
            throw new CalcLayoutException("Nonexistent position.");
        }
        if (position.row == 1 && position.column > 1 && position.column < 6) {
            throw new CalcLayoutException("Illegal position.");
        }

        components.put(position, comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return getSize(target.getInsets(), Component::getMaximumSize);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
        placementParamsNeedUpdate = true;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        components.entrySet().removeIf(rcPositionComponentEntry -> rcPositionComponentEntry.getValue() == comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return getSize(parent.getInsets(), Component::getPreferredSize);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return getSize(parent.getInsets(), Component::getMinimumSize);
    }

    @Override
    public void layoutContainer(Container parent) {
        if(placementParamsNeedUpdate) {
            updatePlacementParameters(parent);
        }
        components.forEach((position, component) -> {
            Rectangle placement = getPlacement(parent, position);
            component.setBounds(placement);
        });
    }

    /**
     * Calculates a placement of a component.
     *
     * @param container Container on which a component will be placed.
     * @param position  Position of a component in the table grid.
     * @return Placement of a component.
     * @throws NullPointerException If either of the arguments are {@code null}.
     */
    private Rectangle getPlacement(Container container, RCPosition position) {
        Objects.requireNonNull(container);
        Objects.requireNonNull(position);

//        coordinates and size of a component
        int x = containerOrigin.x;
        int y = containerOrigin.y;
        x += (position.column - 1) * itemWidth + (position.column - 1) * componentGap + (position.column - 1) / extraPixelDistWidth * deltaX;
        y += (position.row - 1) * itemHeight + (position.row - 1) * componentGap + (position.row - 1) / extraPixelDistHeight * deltaY;
        int height = position.row % extraPixelDistHeight == 0 ? itemHeight + deltaY : itemHeight;
        int width;
        if (position.column == 1 && position.row == 1) {
            width = 5 * itemWidth + 4 * componentGap + 5 / extraPixelDistWidth * deltaX;
        } else {
            width = position.column % extraPixelDistWidth == 0 ? itemWidth + deltaX : itemWidth;
        }

        return new Rectangle(x, y, width, height);
    }

    /**
     * Updates placement parameters of the layout manager.
     *
     * @param container Container whose components are managed by the manager.
     * @throws NullPointerException If given container is {@code null}.
     */
    private void updatePlacementParameters(Container container) {
        Objects.requireNonNull(container);

        Insets containerInsets = container.getInsets();
        Dimension containerDimensions = container.getSize();
        containerDimensions.width -= containerInsets.left + containerInsets.right;
        containerDimensions.height -= containerInsets.top + containerInsets.bottom;

        containerOrigin = new Point(containerInsets.left, containerInsets.top);

//        dimensions of one item
        itemWidth = (int) Math.round((double) (containerDimensions.width - (COLUMN_COUNT - 1) * componentGap) / COLUMN_COUNT);
        itemHeight = (int) Math.round((double) (containerDimensions.height - (ROW_COUNT - 1) * componentGap) / ROW_COUNT);

        /* deltaWidth and deltaHeight are pixels that are over/under width/height of a container e.g. let componentGap = 0,
           container width = 200, itemWidth = 29, COLUMN_COUNT * itemWidth = 203 => deltaX = 200 - 203 = -3 */
        int deltaWidth = containerDimensions.width - ((COLUMN_COUNT - 1) * componentGap + itemWidth * COLUMN_COUNT);
        int deltaHeight = containerDimensions.height - ((ROW_COUNT - 1) * componentGap + itemHeight * ROW_COUNT);

//        Pixel that should be added/subtracted from itemWidth/itemHeight every few items. These will always be -1, 0 or 1.
        deltaX = deltaWidth != 0 ? deltaWidth / Math.abs(deltaWidth) : 0;
        deltaY = deltaHeight != 0 ? deltaHeight / Math.abs(deltaHeight) : 0;

//        How often a deltaX/deltaY pixel should be added to itemWidth/itemHeight
        extraPixelDistWidth = deltaWidth != 0 ? COLUMN_COUNT / Math.abs(deltaWidth) : 1;
        extraPixelDistHeight = deltaHeight != 0 ? ROW_COUNT / Math.abs(deltaHeight) : 1;
    }

    /**
     * Calculates dimension of the layout.
     *
     * @param containerInsets Container's insets that uses the layout.
     * @param sizeSupplier    Function that will take in a component and return its size.
     * @return Ideal dimension of the container that uses this layout.
     * @throws NullPointerException if given function is {@code null}.
     */
    private Dimension getSize(Insets containerInsets, Function<Component, Dimension> sizeSupplier) {
        Objects.requireNonNull(sizeSupplier);

        int componentWidth = -1, componentHeight = -1;
        for (Map.Entry<RCPosition, Component> entry : components.entrySet()) {
//            get dimension
            Dimension dimension = sizeSupplier.apply(entry.getValue());
//            dimension is null -> skip
            if (dimension == null) {
                continue;
            }

            /* Position (1, 1) occupies 5 columns and its width can be more than 5 times bigger than other components.
              We want to consider width of one column, that is why we are dividing total width by number of occupied columns. */
            if (entry.getKey().column == 1 && entry.getKey().row == 1) {
                dimension.width = (dimension.width - componentGap * (5 - 1)) / 5;
            }
//            set max width and height
            componentWidth = Math.max(dimension.width, componentWidth);
            componentHeight = Math.max(dimension.height, componentHeight);
        }
//        If none of the components expressed wanted size -> return null
        if (componentWidth == -1 || componentHeight == -1) {
            return null;
        }

//        Return dimension
        Dimension result = new Dimension();
        result.width = componentWidth * 7 + componentGap * 6 + containerInsets.left + containerInsets.right;
        result.height = componentHeight * 5 + componentGap * 4 + containerInsets.top + containerInsets.bottom;
        return result;
    }
}

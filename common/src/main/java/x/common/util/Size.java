package x.common.util;

/**
 * Author: cxx
 * Date: 2020-08-19
 * GitHub: https://github.com/ccolorcat
 */
public final class Size {
    private final int width;
    private final int height;

    /**
     * Create a new immutable Size instance.
     *
     * @param width  The width of the size, in pixels
     * @param height The height of the size, in pixels
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Get the width of the size (in pixels).
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the size (in pixels).
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return height ^ ((width << (Integer.SIZE / 2)) | (width >>> (Integer.SIZE / 2)));
    }

    /**
     * Check if this size is equal to another size.
     * <p>
     * Two sizes are equal if and only if both their widths and heights are
     * equal.
     * </p>
     * <p>
     * A size object is never equal to any other type of object.
     * </p>
     *
     * @return {@code true} if the objects were equal, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Size) {
            Size other = (Size) obj;
            return width == other.width && height == other.height;
        }
        return false;
    }

    /**
     * Return the size represented as a string with the format {@code "WxH"}
     *
     * @return string representation of the size
     */
    @Override
    public String toString() {
        return width + "x" + height;
    }

    private static NumberFormatException invalidSize(String s) {
        throw new NumberFormatException("Invalid Size: \"" + s + "\"");
    }

    /**
     * Parses the specified string as a size value.
     * <p>
     * The ASCII characters {@code \}{@code u002a} ('*') and
     * {@code \}{@code u0078} ('x') are recognized as separators between
     * the width and height.</p>
     * <p>
     * For any {@code Size s}: {@code Size.parseSize(s.toString()).equals(s)}.
     * However, the method also handles sizes expressed in the
     * following forms:</p>
     * <p>
     * "<i>width</i>{@code x}<i>height</i>" or
     * "<i>width</i>{@code *}<i>height</i>" {@code => new Size(width, height)},
     * where <i>width</i> and <i>height</i> are string integers potentially
     * containing a sign, such as "-10", "+7" or "5".</p>
     *
     * <pre>{@code
     * Size.parseSize("3*+6").equals(new Size(3, 6)) == true
     * Size.parseSize("-3x-6").equals(new Size(-3, -6)) == true
     * Size.parseSize("4 by 3") => throws NumberFormatException
     * }</pre>
     *
     * @param string the string representation of a size value.
     * @return the size value represented by {@code string}.
     * @throws NumberFormatException if {@code string} cannot be parsed
     *                               as a size value.
     * @throws NullPointerException  if {@code string} was {@code null}
     */
    public static Size parseSize(String string) throws NumberFormatException {
        Utils.requireNonNull(string, "string must not be null");

        int sep_ix = string.indexOf('*');
        if (sep_ix < 0) {
            sep_ix = string.indexOf('x');
        }
        if (sep_ix < 0) {
            throw invalidSize(string);
        }
        try {
            int width = Integer.parseInt(string.substring(0, sep_ix));
            int height = Integer.parseInt(string.substring(sep_ix + 1));
            return new Size(width, height);
        } catch (NumberFormatException e) {
            throw invalidSize(string);
        }
    }
}

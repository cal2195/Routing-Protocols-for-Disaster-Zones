package dream.team.assemble.gui;

/**
 *
 * @author Cal
 */
public class Colour
{
    //////////////////////////////////////////////////////////////

    // COLOR FUNCTIONS
    // moved here so that they can work without
    // the graphics actually being instantiated (outside setup)
    /**
     * ( begin auto-generated from colour.xml )
     *
     * Creates colours for storing in variables of the <b>colour</b> datatype.
     * The parameters are interpreted as RGB or HSB values depending on the
     * current
     * <b>colourMode()</b>. The default mode is RGB values from 0 to 255 and
     * therefore, the function call <b>colour(255, 204, 0)</b> will return a
     * bright yellow colour. More about how colours are stored can be found in
     * the reference for the <a href="colour_datatype.html">colour</a> datatype.
     *
     * ( end auto-generated )
     *
     * @param gray number specifying value between white and black
     * @see PApplet#colourMode(int)
     */
    public static final int colour(int gray)
    {
        if (gray > 255)
        {
            gray = 255;
        } else if (gray < 0)
        {
            gray = 0;
        }
        return 0xff000000 | (gray << 16) | (gray << 8) | gray;
    }

    /**
     * @param fgray number specifying value between white and black
     */
    public static final int colour(float fgray)
    {
        int gray = (int) fgray;
        if (gray > 255)
        {
            gray = 255;
        } else if (gray < 0)
        {
            gray = 0;
        }
        return 0xff000000 | (gray << 16) | (gray << 8) | gray;
    }

    /**
     * As of 0116 this also takes colour(#FF8800, alpha)
     *
     * @param alpha relative to current colour range
     */
    public static final int colour(int gray, int alpha)
    {
        if (alpha > 255)
        {
            alpha = 255;
        } else if (alpha < 0)
        {
            alpha = 0;
        }
        if (gray > 255)
        {
            // then assume this is actually a #FF8800
            return (alpha << 24) | (gray & 0xFFFFFF);
        } else
        {
            //if (gray > 255) gray = 255; else if (gray < 0) gray = 0;
            return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
        }
    }

    /**
     * @nowebref
     */
    public static final int colour(float fgray, float falpha)
    {
        int gray = (int) fgray;
        int alpha = (int) falpha;
        if (gray > 255)
        {
            gray = 255;
        } else if (gray < 0)
        {
            gray = 0;
        }
        if (alpha > 255)
        {
            alpha = 255;
        } else if (alpha < 0)
        {
            alpha = 0;
        }
        return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
    }

    /**
     * @param v1 red or hue values relative to the current colour range
     * @param v2 green or saturation values relative to the current colour range
     * @param v3 blue or brightness values relative to the current colour range
     */
    public static final int colour(int v1, int v2, int v3)
    {
        if (v1 > 255)
        {
            v1 = 255;
        } else if (v1 < 0)
        {
            v1 = 0;
        }
        if (v2 > 255)
        {
            v2 = 255;
        } else if (v2 < 0)
        {
            v2 = 0;
        }
        if (v3 > 255)
        {
            v3 = 255;
        } else if (v3 < 0)
        {
            v3 = 0;
        }

        return 0xff000000 | (v1 << 16) | (v2 << 8) | v3;
    }

    public static final int colour(int v1, int v2, int v3, int alpha)
    {
        if (alpha > 255)
        {
            alpha = 255;
        } else if (alpha < 0)
        {
            alpha = 0;
        }
        if (v1 > 255)
        {
            v1 = 255;
        } else if (v1 < 0)
        {
            v1 = 0;
        }
        if (v2 > 255)
        {
            v2 = 255;
        } else if (v2 < 0)
        {
            v2 = 0;
        }
        if (v3 > 255)
        {
            v3 = 255;
        } else if (v3 < 0)
        {
            v3 = 0;
        }

        return (alpha << 24) | (v1 << 16) | (v2 << 8) | v3;
    }

    public static final int colour(float v1, float v2, float v3)
    {
        if (v1 > 255)
        {
            v1 = 255;
        } else if (v1 < 0)
        {
            v1 = 0;
        }
        if (v2 > 255)
        {
            v2 = 255;
        } else if (v2 < 0)
        {
            v2 = 0;
        }
        if (v3 > 255)
        {
            v3 = 255;
        } else if (v3 < 0)
        {
            v3 = 0;
        }

        return 0xff000000 | ((int) v1 << 16) | ((int) v2 << 8) | (int) v3;
    }

    public static final int colour(float v1, float v2, float v3, float alpha)
    {
        if (alpha > 255)
        {
            alpha = 255;
        } else if (alpha < 0)
        {
            alpha = 0;
        }
        if (v1 > 255)
        {
            v1 = 255;
        } else if (v1 < 0)
        {
            v1 = 0;
        }
        if (v2 > 255)
        {
            v2 = 255;
        } else if (v2 < 0)
        {
            v2 = 0;
        }
        if (v3 > 255)
        {
            v3 = 255;
        } else if (v3 < 0)
        {
            v3 = 0;
        }

        return ((int) alpha << 24) | ((int) v1 << 16) | ((int) v2 << 8) | (int) v3;
    }
}

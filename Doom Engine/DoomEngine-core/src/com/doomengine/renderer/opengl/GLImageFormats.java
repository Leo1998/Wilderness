package com.doomengine.renderer.opengl;

import java.util.EnumSet;

import com.doomengine.texture.image.Format;

public class GLImageFormats {

	private GLImageFormats() {
	}

	private static void format(GLImageFormat[][] formatToGL, Format format, int glInternalFormat, int glFormat, int glDataType) {
		formatToGL[0][format.ordinal()] = new GLImageFormat(glInternalFormat, glFormat, glDataType);
	}

	private static void formatSwiz(GLImageFormat[][] formatToGL, Format format, int glInternalFormat, int glFormat, int glDataType) {
		formatToGL[0][format.ordinal()] = new GLImageFormat(glInternalFormat, glFormat, glDataType, false, true);
	}

	private static void formatSrgb(GLImageFormat[][] formatToGL, Format format, int glInternalFormat, int glFormat, int glDataType) {
		formatToGL[1][format.ordinal()] = new GLImageFormat(glInternalFormat, glFormat, glDataType);
	}

	private static void formatSrgbSwiz(GLImageFormat[][] formatToGL, Format format, int glInternalFormat, int glFormat, int glDataType) {
		formatToGL[1][format.ordinal()] = new GLImageFormat(glInternalFormat, glFormat, glDataType, false, true);
	}

	private static void formatComp(GLImageFormat[][] formatToGL, Format format, int glCompressedFormat, int glFormat, int glDataType) {
		formatToGL[0][format.ordinal()] = new GLImageFormat(glCompressedFormat, glFormat, glDataType, true);
	}

	private static void formatCompSrgb(GLImageFormat[][] formatToGL, Format format, int glCompressedFormat, int glFormat, int glDataType) {
		formatToGL[1][format.ordinal()] = new GLImageFormat(glCompressedFormat, glFormat, glDataType, true);
	}

	/**
	 * Generates a list of supported texture formats.
	 * 
	 * The first dimension of the array specifies the colorspace, currently 0
	 * means linear and 1 means sRGB. The second dimension is the ordinal in the
	 * {@link Format image format}.
	 * 
	 * @param caps
	 *            The capabilities for which to determine supported formats.
	 * @return An 2D array containing supported texture formats.
	 */
	public static GLImageFormat[][] getFormatsForCaps(EnumSet<Caps> caps) {
		GLImageFormat[][] formatToGL = new GLImageFormat[2][Format.values().length];

		// Core Profile Formats (supported by both OpenGL Core 3.3 and OpenGL ES
		// 3.0+)
		if (caps.contains(Caps.CoreProfile)) {
			formatSwiz(formatToGL, Format.Alpha8, GL3.GL_R8, GL.GL_RED, GL.GL_UNSIGNED_BYTE);
			formatSwiz(formatToGL, Format.Luminance8, GL3.GL_R8, GL.GL_RED, GL.GL_UNSIGNED_BYTE);
			formatSwiz(formatToGL, Format.Luminance8Alpha8, GL3.GL_RG8, GL3.GL_RG, GL.GL_UNSIGNED_BYTE);
			formatSwiz(formatToGL, Format.Luminance16F, GL3.GL_R16F, GL.GL_RED, GLExt.GL_HALF_FLOAT_ARB);
			formatSwiz(formatToGL, Format.Luminance32F, GL3.GL_R32F, GL.GL_RED, GL.GL_FLOAT);
			formatSwiz(formatToGL, Format.Luminance16FAlpha16F, GL3.GL_RG16F, GL3.GL_RG, GLExt.GL_HALF_FLOAT_ARB);

			formatSrgbSwiz(formatToGL, Format.Luminance8, GLExt.GL_SRGB8_EXT, GL.GL_RED, GL.GL_UNSIGNED_BYTE);
			formatSrgbSwiz(formatToGL, Format.Luminance8Alpha8, GLExt.GL_SRGB8_ALPHA8_EXT, GL3.GL_RG, GL.GL_UNSIGNED_BYTE);
		}

		if (caps.contains(Caps.OpenGL20)) {
			if (!caps.contains(Caps.CoreProfile)) {
				format(formatToGL, Format.Alpha8, GL2.GL_ALPHA8, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE);
				format(formatToGL, Format.Luminance8, GL2.GL_LUMINANCE8, GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE);
				format(formatToGL, Format.Luminance8Alpha8, GL2.GL_LUMINANCE8_ALPHA8, GL.GL_LUMINANCE_ALPHA, GL.GL_UNSIGNED_BYTE);
			}
			format(formatToGL, Format.RGB8, GL2.GL_RGB8, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
			format(formatToGL, Format.RGBA8, GLExt.GL_RGBA8, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
			format(formatToGL, Format.RGB565, GL2.GL_RGB8, GL.GL_RGB, GL.GL_UNSIGNED_SHORT_5_6_5);

			// Additional desktop-specific formats:
			format(formatToGL, Format.BGR8, GL2.GL_RGB8, GL2.GL_BGR, GL.GL_UNSIGNED_BYTE);
			format(formatToGL, Format.ARGB8, GLExt.GL_RGBA8, GL2.GL_BGRA, GL2.GL_UNSIGNED_INT_8_8_8_8);
			format(formatToGL, Format.BGRA8, GLExt.GL_RGBA8, GL2.GL_BGRA, GL.GL_UNSIGNED_BYTE);
			format(formatToGL, Format.ABGR8, GLExt.GL_RGBA8, GL.GL_RGBA, GL2.GL_UNSIGNED_INT_8_8_8_8);

			// sRGB formats
			if (caps.contains(Caps.Srgb)) {
				formatSrgb(formatToGL, Format.RGB8, GLExt.GL_SRGB8_EXT, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
				formatSrgb(formatToGL, Format.RGB565, GLExt.GL_SRGB8_EXT, GL.GL_RGB, GL.GL_UNSIGNED_SHORT_5_6_5);
				formatSrgb(formatToGL, Format.RGB5A1, GLExt.GL_SRGB8_ALPHA8_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_SHORT_5_5_5_1);
				formatSrgb(formatToGL, Format.RGBA8, GLExt.GL_SRGB8_ALPHA8_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
				if (!caps.contains(Caps.CoreProfile)) {
					formatSrgb(formatToGL, Format.Luminance8, GLExt.GL_SLUMINANCE8_EXT, GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE);
					formatSrgb(formatToGL, Format.Luminance8Alpha8, GLExt.GL_SLUMINANCE8_ALPHA8_EXT, GL.GL_LUMINANCE_ALPHA, GL.GL_UNSIGNED_BYTE);
				}
				formatSrgb(formatToGL, Format.BGR8, GLExt.GL_SRGB8_EXT, GL2.GL_BGR, GL.GL_UNSIGNED_BYTE);
				formatSrgb(formatToGL, Format.ABGR8, GLExt.GL_SRGB8_ALPHA8_EXT, GL.GL_RGBA, GL2.GL_UNSIGNED_INT_8_8_8_8);
				formatSrgb(formatToGL, Format.ARGB8, GLExt.GL_SRGB8_ALPHA8_EXT, GL2.GL_BGRA, GL2.GL_UNSIGNED_INT_8_8_8_8);
				formatSrgb(formatToGL, Format.BGRA8, GLExt.GL_SRGB8_ALPHA8_EXT, GL2.GL_BGRA, GL.GL_UNSIGNED_BYTE);

				if (caps.contains(Caps.TextureCompressionS3TC)) {
					formatCompSrgb(formatToGL, Format.DXT1, GLExt.GL_COMPRESSED_SRGB_S3TC_DXT1_EXT, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
					formatCompSrgb(formatToGL, Format.DXT1A, GLExt.GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT1_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
					formatCompSrgb(formatToGL, Format.DXT3, GLExt.GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT3_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
					formatCompSrgb(formatToGL, Format.DXT5, GLExt.GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT5_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
				}
			}
		} else if (caps.contains(Caps.Rgba8)) {
			// A more limited form of 32-bit RGBA. Only GL_RGBA8 is available.
			if (!caps.contains(Caps.CoreProfile)) {
				format(formatToGL, Format.Alpha8, GLExt.GL_RGBA8, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE);
				format(formatToGL, Format.Luminance8, GLExt.GL_RGBA8, GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE);
				format(formatToGL, Format.Luminance8Alpha8, GLExt.GL_RGBA8, GL.GL_LUMINANCE_ALPHA, GL.GL_UNSIGNED_BYTE);
			}
			format(formatToGL, Format.RGB8, GLExt.GL_RGBA8, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
			format(formatToGL, Format.RGBA8, GLExt.GL_RGBA8, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
		} else {
			// Actually, the internal format isn't used for OpenGL ES 2! This is
			// the same as the above..
			if (!caps.contains(Caps.CoreProfile)) {
				format(formatToGL, Format.Alpha8, GL.GL_RGBA4, GL.GL_ALPHA, GL.GL_UNSIGNED_BYTE);
				format(formatToGL, Format.Luminance8, GL.GL_RGB565, GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE);
				format(formatToGL, Format.Luminance8Alpha8, GL.GL_RGBA4, GL.GL_LUMINANCE_ALPHA, GL.GL_UNSIGNED_BYTE);
			}
			format(formatToGL, Format.RGB8, GL.GL_RGB565, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
			format(formatToGL, Format.RGBA8, GL.GL_RGBA4, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
		}

		if (caps.contains(Caps.OpenGLES20)) {
			format(formatToGL, Format.RGB565, GL.GL_RGB565, GL.GL_RGB, GL.GL_UNSIGNED_SHORT_5_6_5);
		}

		format(formatToGL, Format.RGB5A1, GL.GL_RGB5_A1, GL.GL_RGBA, GL.GL_UNSIGNED_SHORT_5_5_5_1);

		if (caps.contains(Caps.FloatTexture)) {
			if (!caps.contains(Caps.CoreProfile)) {
				format(formatToGL, Format.Luminance16F, GLExt.GL_LUMINANCE16F_ARB, GL.GL_LUMINANCE, GLExt.GL_HALF_FLOAT_ARB);
				format(formatToGL, Format.Luminance32F, GLExt.GL_LUMINANCE32F_ARB, GL.GL_LUMINANCE, GL.GL_FLOAT);
				format(formatToGL, Format.Luminance16FAlpha16F, GLExt.GL_LUMINANCE_ALPHA16F_ARB, GL.GL_LUMINANCE_ALPHA, GLExt.GL_HALF_FLOAT_ARB);
			}
			format(formatToGL, Format.RGB16F, GLExt.GL_RGB16F_ARB, GL.GL_RGB, GLExt.GL_HALF_FLOAT_ARB);
			format(formatToGL, Format.RGB32F, GLExt.GL_RGB32F_ARB, GL.GL_RGB, GL.GL_FLOAT);
			format(formatToGL, Format.RGBA16F, GLExt.GL_RGBA16F_ARB, GL.GL_RGBA, GLExt.GL_HALF_FLOAT_ARB);
			format(formatToGL, Format.RGBA32F, GLExt.GL_RGBA32F_ARB, GL.GL_RGBA, GL.GL_FLOAT);
		}
		if (caps.contains(Caps.PackedFloatTexture)) {
			format(formatToGL, Format.RGB111110F, GLExt.GL_R11F_G11F_B10F_EXT, GL.GL_RGB, GLExt.GL_UNSIGNED_INT_10F_11F_11F_REV_EXT);
			if (caps.contains(Caps.FloatTexture)) {
				format(formatToGL, Format.RGB16F_to_RGB111110F, GLExt.GL_R11F_G11F_B10F_EXT, GL.GL_RGB, GLExt.GL_HALF_FLOAT_ARB);
			}
		}
		if (caps.contains(Caps.SharedExponentTexture)) {
			format(formatToGL, Format.RGB9E5, GLExt.GL_RGB9_E5_EXT, GL.GL_RGB, GLExt.GL_UNSIGNED_INT_5_9_9_9_REV_EXT);
			if (caps.contains(Caps.FloatTexture)) {
				format(formatToGL, Format.RGB16F_to_RGB9E5, GLExt.GL_RGB9_E5_EXT, GL.GL_RGB, GLExt.GL_HALF_FLOAT_ARB);
			}
		}

		// Need to check if Caps.DepthTexture is supported prior to using for
		// textures
		// But for renderbuffers its OK.
		format(formatToGL, Format.Depth, GL.GL_DEPTH_COMPONENT, GL.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_BYTE);
		format(formatToGL, Format.Depth16, GL.GL_DEPTH_COMPONENT16, GL.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_SHORT);

		if (caps.contains(Caps.OpenGL20)) {
			format(formatToGL, Format.Depth24, GL2.GL_DEPTH_COMPONENT24, GL.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_INT);
		}
		if (caps.contains(Caps.FloatDepthBuffer)) {
			format(formatToGL, Format.Depth32F, GLExt.GL_DEPTH_COMPONENT32F, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT);
		}
		if (caps.contains(Caps.PackedDepthStencilBuffer)) {
			format(formatToGL, Format.Depth24Stencil8, GLExt.GL_DEPTH24_STENCIL8_EXT, GLExt.GL_DEPTH_STENCIL_EXT, GLExt.GL_UNSIGNED_INT_24_8_EXT);
		}

		// Compressed formats
		if (caps.contains(Caps.TextureCompressionS3TC)) {
			formatComp(formatToGL, Format.DXT1, GLExt.GL_COMPRESSED_RGB_S3TC_DXT1_EXT, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
			formatComp(formatToGL, Format.DXT1A, GLExt.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
			formatComp(formatToGL, Format.DXT3, GLExt.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
			formatComp(formatToGL, Format.DXT5, GLExt.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE);
		}

		if (caps.contains(Caps.TextureCompressionETC2)) {
			formatComp(formatToGL, Format.ETC1, GLExt.GL_COMPRESSED_RGB8_ETC2, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
		} else if (caps.contains(Caps.TextureCompressionETC1)) {
			formatComp(formatToGL, Format.ETC1, GLExt.GL_ETC1_RGB8_OES, GL.GL_RGB, GL.GL_UNSIGNED_BYTE);
		}

		return formatToGL;
	}

}

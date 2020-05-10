package utils;

import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLoader {

	public static int createTexture(String path) {
		return createTexture(path, false);
	}

	public static int createTexture(String path, boolean isCubemap) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			final InputStream in = FileUtils.getInputStream(path);
			final PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.BGRA);
			buffer.flip();
			in.close();
		} catch (final Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + path + " , didn't work");
			return 0;
		}

		return loadTextureToOpenGL(width, height, buffer, isCubemap ? GL13.GL_TEXTURE_CUBE_MAP : GL11.GL_TEXTURE_2D);
	}

	protected static int loadTextureToOpenGL(int width, int height, ByteBuffer data, int type) {
		final int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(type, texID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		if (type == GL13.GL_TEXTURE_CUBE_MAP) {
			for (int i = 0; i < 6; i++) {
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, width, height, 0,
						GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data);
			}
		} else {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA,
					GL11.GL_UNSIGNED_BYTE, data);
		}
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return texID;
	}

	public static void unbindTexture() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}

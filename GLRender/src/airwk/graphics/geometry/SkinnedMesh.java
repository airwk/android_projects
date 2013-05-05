package airwk.graphics.geometry;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glGenBuffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class SkinnedMesh extends Mesh
{
	private FloatBuffer bwb;
	private ShortBuffer bib;

	private float[] boneWeights;
	private short[] boneIndices;

	public SkinnedMesh(float[] vertices, float[] normals, float[] uv, int[] indices, float[] boneWeights, short[] boneIndices)
	{
		super(Geometry.SKINNED_MESH, vertices, normals, uv, indices);

		this.boneWeights = boneWeights;
		this.boneIndices = boneIndices;
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		super.resume();

		bwb = ByteBuffer.allocateDirect(boneWeights.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		bwb.put(boneWeights).position(0);

		bib = ByteBuffer.allocateDirect(boneIndices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
		bib.put(boneIndices).position(0);

		int[] buffers = new int[2];

		glGenBuffers(buffers.length, buffers, 0);

		glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
		glBufferData(GL_ARRAY_BUFFER, bwb.capacity() * 4, bwb, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
		glBufferData(GL_ARRAY_BUFFER, bib.capacity() * 2, bib, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		setBuffer(2, buffers[0]);
		setBuffer(3, buffers[1]);
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		super.destroy();

		bib.clear();
		bib = null;

		bwb.clear();
		bwb = null;

		boneIndices = null;
		boneWeights = null;
		buffers = null;
	}
}

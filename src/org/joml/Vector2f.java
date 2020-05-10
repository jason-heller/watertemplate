/*
 * (C) Copyright 2015 Richard Greenlees

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */
package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

/**
 *
 * @author RGreenlees
 * @author Kai Burjack
 */
public class Vector2f implements Serializable, Externalizable {

	public static final Vector2f X_AXIS = new Vector2f(1,0);
	public static final Vector2f Y_AXIS = new Vector2f(0,1);
	public float x;
	public float y;

	public Vector2f() {
	}

	public Vector2f(float newX, float newY) {
		x = newX;
		y = newY;
	}

	public Vector2f(Vector2f clone) {
		x = clone.x;
		y = clone.y;
	}

	/** Sets the X and Y attributes using the supplied float values */
	public void set(float newX, float newY) {
		x = newX;
		y = newY;
	}

	/** Sets this Vector2f to be a clone of v */
	public void set(Vector2f v) {
		x = v.x;
		y = v.y;
	}

	/** Stores the perpendicular of v in dest. Does not modify v */
	public static void perpendicular(Vector2f v, Vector2f dest) {
		dest.x = v.y;
		dest.y = v.x * -1;
	}

	/** Sets this Vector2f to be its perpendicular */
	public void perpendicular() {
		set(-y, x);
	}

	/** Subtracts b from a and stores the result in dest. Does not modify a or b */
	public static Vector2f sub(Vector2f a, Vector2f b) {
		return new Vector2f(a.x - b.x, a.y - b.y);
	}

	/** Subtracts v from this Vector2f */
	public void sub(Vector2f v) {
		x -= v.x;
		y -= v.y;
	}

	/** Returns the dot product of a and b */
	public static float dot(Vector2f a, Vector2f b) {
		return ((a.x * b.x) + (a.y * b.y));
	}

	/** Returns the dot product of this vector and v */
	public float dot(Vector2f v) {
		return ((x * v.x) + (y * v.y));
	}

	/** Returns the length of a */
	public static float length(Vector2f a) {
		return (float) Math.sqrt((a.x * a.x) + (a.y * a.y));
	}

	/** Returns the length of this Vector2f */
	public float length() {
		return (float) Math.sqrt((x * x) + (y * y));
	}

	public float lengthSquared() {
		return ((x * x) + (y * y));
	}

	/** Returns the distance between the start and end vectors */
	public static float distance(Vector2f start, Vector2f end) {
		return (float) Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));
	}

	/** Returns the distance between this Vector and v */
	public float distance(Vector2f v) {
		return (float) Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y));
	}

	/**
	 * Stores a normalized copy of the supplied Vector2f in dest. Does not modify a
	 */
	public static void normalize(Vector2f a, Vector2f dest) {
		float length = (float) Math.sqrt((a.x * a.x) + (a.y * a.y));
		dest.x = a.x / length;
		dest.y = a.y / length;
	}

	/** Normalizes this Vector2f */
	public void normalize() {
		float length = (float) Math.sqrt((x * x) + (y * y));
		x /= length;
		y /= length;
	}

	/** Adds v to this Vector2f */
	public void add(Vector2f v) {
		x += v.x;
		y += v.y;
	}

	/** Adds b to a and stores the results in dest */
	public static Vector2f add(Vector2f a, Vector2f b) {
		Vector2f dest = new Vector2f();
		dest.x = a.x + b.x;
		dest.y = a.y + b.y;
		return dest;
	}

	/**
	 * Set all components to zero.
	 */
	public void zero() {
		this.x = 0.0f;
		this.y = 0.0f;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(x);
		out.writeFloat(y);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		x = in.readFloat();
		y = in.readFloat();
	}

	/**
	 * Negate this vector.
	 * 
	 * @return this
	 */
	public Vector2f negate() {
		x = -x;
		y = -y;
		return this;
	}

	public void mul(float f) {
		x *= f;
		y *= f;
	}

	public float distToLine(Vector2f start, Vector2f end) {
		float A = x - start.x; // position of point rel one end of line
		float B = y - start.y;
		float C = end.x - start.x; // vector along line
		float D = end.y - start.y;
		float E = -D; // orthogonal vector
		float F = C;
		float len_sq = E * E + F * F;
		float dot = A * E + B * F;

		return (float) (Math.abs(dot) / Math.sqrt(len_sq));
	}

	public float signedDistToLineSquared(Vector2f start, Vector2f end) {
		float A = x - start.x; // position of point rel one end of line
		float B = y - start.y;
		float C = end.x - start.x; // vector along line
		float D = end.y - start.y;
		float E = -D; // orthogonal vector
		float F = C;

		float dot = A * E + B * F;

		return dot;
	}

	public void div(float d) {
		x /= d;
		y /= d;
	}

	public void add(float a, float b) {
		x += a;
		y += b;
	}

	public boolean isZero() {
		return ( x==0f&&y==0f);
	}

	public void abs() {
		x = Math.abs(x);
		y = Math.abs(y);
	}

}

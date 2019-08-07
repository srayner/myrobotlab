/**
 * 
 */
package org.myrobotlab.IntegratedMovement;

import java.util.HashMap;

import org.myrobotlab.kinematics.DHLink;
import org.myrobotlab.kinematics.DHLinkType;
import org.myrobotlab.kinematics.Matrix;
import org.myrobotlab.kinematics.Point;
import org.myrobotlab.math.MathUtils;

/**
 * Contain Info about a part or object that can be used by IntegratedMovement
 * @author calamity
 *
 */
public class IMPart {

	
	private String name;
	private HashMap<String,String> controls = new HashMap<String, String>();
	private HashMap<String,DHLink> DHLinks = new HashMap<String, DHLink>();
	private Double radius = 0.1;
	private String modelPath;
	private float scale = 1;
	private Point initialTranslateRotate = new Point(0,0,0,0,0,0);
	private Matrix origin = IMUtil.getIdentityMatrix();
	private Matrix end = IMUtil.getIdentityMatrix();
	private boolean visible = true;
	private Matrix internTransform = IMUtil.getIdentityMatrix();
	private double theta;
	private double alpha;
	private double initialTheta;
	private double r;

	public IMPart(String partName){
		name = partName;
	}

	

	public String getName() {
		return name;
	}



	public void setControl(String armModel, String control) {
		controls.put(armModel, control);
	}



	public void setDHParameters(String armModel, double d, double theta, double r, double alpha) {
		setDHParameters(armModel, d, theta, r, alpha, DHLinkType.REVOLUTE);
	}



	public String getControl(String armName) {
		return controls.get(armName);
	}



	public HashMap<String, String> getControls() {
		return controls;
	}



	public DHLink getDHLink(String armName) {
		return DHLinks.get(armName);
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}



	public void set3DModel(String modelPath, float scale, Point initialTranslateRotate) {
		this.modelPath = modelPath;
		this.scale = scale;		
		this.initialTranslateRotate = initialTranslateRotate;
	}



	public void setOrigin(Matrix m) {
		origin  = m;	
	}



	public void setEnd(Matrix m) {
		end  = m;
	}



	public String get3DModelPath() {
		return modelPath;
	}



	public float getScale() {
		return scale;
	}



	public Point getInitialTranslateRotate() {
		return initialTranslateRotate;
	}



	/**
	 * @return the origin
	 */
	public Matrix getOrigin() {
		return origin;
	}



	public Matrix getEnd() {
		return end;
	}



	public void setDHType(String arm, DHLinkType dhLinkType) {
		DHLinks.get(arm).setType(dhLinkType);
	}



	public double getRadius() {
		return radius;
	}
	
	public Point getOriginPoint(){
		return IMUtil.matrixToPoint(origin);
	}



	public Point getEndPoint() {
		return IMUtil.matrixToPoint(end);
	}
	
	public double getLength() {
		Point origin = getOriginPoint();
		Point end = getEndPoint();
		return (Math.sqrt(Math.pow(origin.getX() - end.getX(), 2) + Math.pow(origin.getY() - end.getY(), 2) + Math.pow(origin.getZ() - end.getZ(), 2)));
	}



	public void setVisible(boolean b) {
		visible  = b;
	}



	public boolean isVisible() {
		return visible;
	}



	public void setDHParameters(String armName, double d, double theta, double r, double alpha, DHLinkType dhLinkType) {
		DHLink link = new DHLink(name, d, r, MathUtils.degToRad(theta), MathUtils.degToRad(alpha), dhLinkType);
		DHLinks.put(armName, link);
	}



	/**
	 * @return the internTransform
	 */
	public Matrix getInternTransform() {
		return internTransform;
	}



	/**
	 * @param internTransform the internTransform to set
	 */
	public void setInternTransform(Matrix internTransform) {
		this.internTransform = internTransform;
	}



	public void setTheta(double theta) {
		this.theta = theta;
	}



	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public double getTheta(){
		return theta;
	}
	
	public double getAlpha(){
		return alpha;
	}



	public void setInitialTheta(double rad) {
		initialTheta = rad;
	}

	public double getInitialTheta(){
		return initialTheta;
	}



	public double getR() {
		return r;
	}



	public void setR(double r) {
		this.r = r;
		
	}



	public Matrix transform(String armName) {
		return DHLinks.get(armName).resolveMatrix();
	}
}

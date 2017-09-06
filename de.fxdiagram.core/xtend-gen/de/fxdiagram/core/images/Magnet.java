package de.fxdiagram.core.images;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Magnet extends Parent {
  public Magnet() {
    ObservableList<Node> _children = this.getChildren();
    Group _createGroup = this.createGroup();
    _children.add(_createGroup);
  }
  
  protected Group createGroup() {
    Group _group = new Group();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      SVGPath _sVGPath = new SVGPath();
      final Procedure1<SVGPath> _function_1 = (SVGPath it_1) -> {
        it_1.setContent("m 67.697,119.816 c 34.942,0 61.557,-25.644 64.317,-60.226 1.58,-19.809 -0.918,-40.315 -10.865,-57.748 -2.611,-1.459 -10.149,0 -13.112,0 -2.762,0 -9.751,-1.393 -11.364,1.805 -2.679,5.304 0.127,14.024 1.094,19.446 1.58,8.87 2.682,17.375 1.955,26.402 C 98.667,62.582 93.828,74.029 79.97,77.008 68.158,79.542 50.695,79.524 41.716,70.046 31.707,59.486 32.691,40.873 35.546,27.795 37.044,20.928 41.103,11.485 39.255,4.395 38.152,0.16 29.883,1.76 26.766,1.724 22.368,1.669 13.742,-0.486 11.5,3.723 6.034,13.971 2.801,24.95 1.681,36.504 -0.396,57.938 2.01,79.972 16.382,96.975 c 12.653,14.969 31.89,22.841 51.315,22.841");
        Color _color = Color.color(0.9098039215686274, 
          0.5019607843137255, 
          0.5019607843137255);
        Stop _stop = new Stop(0, _color);
        Color _color_1 = Color.color(0.8705882352941177, 
          0.2823529411764706, 
          0.2823529411764706);
        Stop _stop_1 = new Stop(0.6181, _color_1);
        Color _color_2 = Color.color(0.8470588235294118, 
          0.16470588235294117, 
          0.16470588235294117);
        Stop _stop_2 = new Stop(1.0, _color_2);
        RadialGradient _radialGradient = new RadialGradient(
          0, 
          0, 
          100.626498, 
          98.81099900000001, 
          (-120.088), 
          false, 
          CycleMethod.NO_CYCLE, _stop, _stop_1, _stop_2);
        it_1.setFill(_radialGradient);
      };
      SVGPath _doubleArrow = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Node> _children_1 = it.getChildren();
      SVGPath _sVGPath_1 = new SVGPath();
      final Procedure1<SVGPath> _function_2 = (SVGPath it_1) -> {
        it_1.setContent("m 131.271,37.805 c -0.921,-9.047 -2.576,-18.107 -5.783,-26.644 -0.451,-1.198 -3.217,-9.319 -4.482,-9.319 -1.821,0 -3.643,0 -5.464,0 -7.674,0 -19.95,-3.54 -19.67,7.253 0.196,7.574 2.227,15.067 3.336,22.535 0.972,6.537 0.015,6.175 6.214,6.175 8.616,0 17.233,0 25.849,0 m -97.403,0 C 34.747,27.882 38.695,18.454 39.458,8.537 39.986,1.672 36.51,1.845 31.034,1.775 24.963,1.7 18.893,1.625 12.819,1.546 10.922,1.525 6.982,14.191 6.392,15.979 4.064,23.047 2.602,30.396 1.914,37.805 c 10.651,0 21.303,0 31.954,0");
        it_1.setFill(Color.color(
          0.8980392156862745, 
          0.8980392156862745, 
          0.8980392156862745));
      };
      SVGPath _doubleArrow_1 = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath_1, _function_2);
      _children_1.add(_doubleArrow_1);
      ObservableList<Node> _children_2 = it.getChildren();
      SVGPath _sVGPath_2 = new SVGPath();
      final Procedure1<SVGPath> _function_3 = (SVGPath it_1) -> {
        it_1.setContent("m 133.151,52.608 c 0,-10.226 -0.443,-20.501 -2.961,-30.46 -1.778,-7.036 -4.261,-21.252 -12.984,-21.252 -5.15,0 -12.748,-1.488 -17.681,0 -8.931,0.117 -2.733,21.604 -1.993,25.854 2.313,13.259 4.679,35.693 -7.748,45.308 -9.198,7.116 -26.049,6.385 -36.661,3.605 C 39.022,71.969 35.268,58.664 34.685,45.513 34.203,34.58 37.529,24.487 39.56,13.869 40.189,10.58 41.463,5.482 39.337,2.441 37.755,0.184 33.678,0.863 31.339,0.834 27.581,0.784 13.765,-1.594 11.483,1.913 6.005,10.315 3.005,21.695 1.492,31.503 c -2.938,19.016 -2.112,39.8 7.687,56.819 14.843,25.779 46.697,37.219 75.123,30.58 30.927,-7.221 48.849,-35.539 48.849,-66.294 m -1.727,0 c 0,34.868 -23.729,64.123 -59.447,66.145 C 37.28,120.713 6.675,96.957 2.45,62.03 -0.002,41.766 3.344,20.42 13.206,2.498 17.969,2.552 22.73,2.619 27.494,2.677 33.251,2.75 39.301,1.041 38.56,8.881 36.765,27.852 27.852,46.552 37.184,65.198 43.307,77.432 57.155,79.253 69.507,79.039 80.514,78.847 91.865,76.468 96.929,65.583 104.34,49.652 99.668,31.582 97.324,15.162 96.587,9.996 94.994,2.788 101.844,2.788 c 5.683,0 11.365,0 17.051,0 5.063,0 8.566,15.372 9.658,19.741 2.458,9.829 2.871,19.991 2.871,30.079");
        Color _color = Color.color(
          0.5686274509803921, 
          0.5686274509803921, 
          0.5686274509803921);
        Stop _stop = new Stop(
          0.0, _color);
        Color _color_1 = Color.color(
          0.5686274509803921, 
          0.5686274509803921, 
          0.5686274509803921);
        Stop _stop_1 = new Stop(
          0.0056, _color_1);
        Color _color_2 = Color.color(
          0.7490196078431373, 
          0.050980392156862744, 
          0.0);
        Stop _stop_2 = new Stop(
          1.0, _color_2);
        LinearGradient _linearGradient = new LinearGradient(
          66.5670386869997, 
          35.36958841299975, 
          66.5670386869997, 
          58.57926640300002, 
          false, 
          CycleMethod.NO_CYCLE, _stop, _stop_1, _stop_2);
        it_1.setFill(_linearGradient);
      };
      SVGPath _doubleArrow_2 = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath_2, _function_3);
      _children_2.add(_doubleArrow_2);
      ObservableList<Node> _children_3 = it.getChildren();
      SVGPath _sVGPath_3 = new SVGPath();
      final Procedure1<SVGPath> _function_4 = (SVGPath it_1) -> {
        it_1.setContent("m 13.842,83.873 c 0,2.83 9.374,10.294 11.391,12.021 10.726,9.183 24.172,14.539 38.305,15.052 14.944,0.539 29.649,-3.849 42.247,-11.796 4.328,-2.729 8.4,-5.854 12.21,-9.27 0.515,-0.461 3.912,-4.399 4.675,-4.179 0.612,0.177 -12.533,14.285 -13.504,15.072 -12.204,9.93 -27.383,15.891 -43.197,15.891 -14.793,0 -28.845,-5.701 -39.604,-15.808 C 22.872,97.576 13.842,89.375 13.842,83.873");
        it_1.setFill(Color.color(
          1.0, 
          0.6392156862745098, 
          0.6392156862745098));
      };
      SVGPath _doubleArrow_3 = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath_3, _function_4);
      _children_3.add(_doubleArrow_3);
      ObservableList<Node> _children_4 = it.getChildren();
      SVGPath _sVGPath_4 = new SVGPath();
      final Procedure1<SVGPath> _function_5 = (SVGPath it_1) -> {
        it_1.setContent("m 34.692,64.009 c 0.196,2.275 6.695,7.454 8.483,8.884 8.192,6.55 18.279,8.777 28.609,7.948 9.901,-0.796 26.275,-5.766 29.778,-16.91 -3.592,11.425 -14.2,19.624 -25.682,21.923 -15.078,3.015 -39.553,-3 -41.188,-21.845");
        it_1.setFill(Color.color(
          0.6980392156862745, 
          0.050980392156862744, 
          0.050980392156862744));
      };
      SVGPath _doubleArrow_4 = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath_4, _function_5);
      _children_4.add(_doubleArrow_4);
      ObservableList<Node> _children_5 = it.getChildren();
      SVGPath _sVGPath_5 = new SVGPath();
      final Procedure1<SVGPath> _function_6 = (SVGPath it_1) -> {
        it_1.setContent("m 102.4,8.009 c 2.229,-4.608 16.587,7.405 16.066,9.742 0.063,-0.283 -4.482,-2.673 -5.126,-2.601 -1.48,0.167 6.305,14.689 7.322,14.479 -3.646,0.757 -11.687,-5.402 -13.663,-8.28 -1.714,-2.495 -6.164,-10.088 -4.599,-13.34");
        it_1.setFill(Color.WHITE);
      };
      SVGPath _doubleArrow_5 = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath_5, _function_6);
      _children_5.add(_doubleArrow_5);
      ObservableList<Node> _children_6 = it.getChildren();
      SVGPath _sVGPath_6 = new SVGPath();
      final Procedure1<SVGPath> _function_7 = (SVGPath it_1) -> {
        it_1.setContent("M 19.027,34.021 C 17.605,32.25 15.848,30.488 13.76,29.528 9.386,27.516 10.079,32.158 7.441,32.896 9.124,32.425 12.339,5.814 19.403,13.057 c 4.221,4.326 13.22,27.401 -0.376,20.964");
        it_1.setFill(Color.WHITE);
      };
      SVGPath _doubleArrow_6 = ObjectExtensions.<SVGPath>operator_doubleArrow(_sVGPath_6, _function_7);
      _children_6.add(_doubleArrow_6);
      ObservableList<Transform> _transforms = it.getTransforms();
      Affine _affine = new Affine();
      final Procedure1<Affine> _function_8 = (Affine it_1) -> {
        it_1.setMxx((-0.1));
        it_1.setMyx(0);
        it_1.setMxy(0);
        it_1.setMyy((-0.1));
        it_1.setTx(13.315098);
        it_1.setTy(12.075432);
      };
      Affine _doubleArrow_7 = ObjectExtensions.<Affine>operator_doubleArrow(_affine, _function_8);
      _transforms.add(_doubleArrow_7);
    };
    return ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
  }
}

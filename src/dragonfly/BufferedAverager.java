package dragonfly;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author S
 */
public class BufferedAverager {
    double[] data; 
    int i = 0;
    int size = 0;
    public BufferedAverager(int size_in){
        data = new double[size_in];
        this.size = size_in;
    }
    
    public void addSample(double sample){
        data[i]=sample;
        i++;
        if (i >= size){
            i = 0;
        }
    }
    public double getAverage(){
        double mean = 0;
        for(int iter =0; iter < size; iter++){
            mean += data[iter];
        }
        mean = mean/size;
        return mean;
    }
}

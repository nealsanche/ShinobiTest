package com.robotsandpencils.shinobitest;

import com.shinobicontrols.charts.Data;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * A time series data adapter, which saves data into a tree structure so that inserted data
 * is always in the correct order.
 * <p/>
 * Created by neal on 2014-06-16.
 */
public class DomainDataAdapter extends DataAdapter<Double, Double> {

    final TreeSet<Data<Double, Double>> mSet = new TreeSet<>(new Comparator<Data<Double, Double>>() {
        @Override
        public int compare(Data<Double, Double> lhs, Data<Double, Double> rhs) {
            return lhs.getY().compareTo(rhs.getY());
        }
    });
    final ArrayList<Data<Double, Double>> mList = new ArrayList<>();

    private Data<Double, Double> mStart = null;
    private Data<Double, Double> mEnd = null;

    boolean mIsDirty = false;

    Runnable mUpdateHandler = new Runnable() {
        public void run() {
            fireUpdateHandler();
        }
    };

    public void fireUpdates() {
        if (mIsDirty) {
            mList.clear();
            if (mStart != null && mEnd != null) {
                mList.addAll(mSet.subSet(mStart, mEnd));
            } else {
                mList.addAll(mSet);
            }

            mIsDirty = false;
            mUpdateHandler.run();
        }
    }

    @Override
    public boolean add(Data<Double, Double> dataPoint) {
        mIsDirty = true;
        return mSet.add(dataPoint);
    }

    @Override
    public void add(int location, Data<Double, Double> dataPoint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Data<Double, Double>> dataPoints) {
        mIsDirty = true;
        return mSet.addAll(dataPoints);
    }

    @Override
    public boolean addAll(int location, Collection<? extends Data<Double, Double>> dataPoints) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        mIsDirty = true;
        mSet.clear();
    }

    @Override
    public boolean contains(Object object) {
        return mSet.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return mSet.containsAll(collection);
    }

    @Override
    public Data<Double, Double> get(int location) {
        final Iterator<Data<Double, Double>> iterator = mSet.iterator();
        for (int i = 0; i < (location - 1); i++) {
            if (iterator.hasNext()) {
                iterator.next();
            }
        }

        return iterator.next();
    }

    @Override
    public int hashCode() {
        return mSet.hashCode();
    }

    @Override
    public int indexOf(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return mSet.isEmpty();
    }

    @Override
    public Iterator<Data<Double, Double>> iterator() {
        return mSet.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Data<Double, Double> remove(int location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object object) {
        mIsDirty = true;
        return mSet.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        mIsDirty = true;
        return mSet.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        mIsDirty = true;
        return mSet.retainAll(collection);
    }

    @Override
    public Data<Double, Double> set(int location, Data<Double, Double> dataPoint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return mSet.size();
    }

    @Override
    public Object[] toArray() {
        return mSet.toArray();
    }

    @Override
    public List<Data<Double, Double>> getDataPointsForDisplay() {
        return mList;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return mSet.toArray(array);
    }

    public Double getInterpolatedValueByDomainValue(Double value) {

        if (value == null) {
            return null;
        }

        double searchTime = -1 * value;

        final Iterator<Data<Double, Double>> iterator = mSet.iterator();

        Data<Double, Double> bottomData = null;
        Data<Double, Double> topData = null;

        while (iterator.hasNext()) {

            if (topData == null) {
                topData = iterator.next();
                continue;
            }

            bottomData = iterator.next();

            double bottomTime = -1 * bottomData.getY();
            double topTime = -1 * topData.getY();

            if (searchTime > topTime) {
                return null;
            }

            if (searchTime >= bottomTime && searchTime < topTime) {
                return bottomData.getX() + (topData.getX() - bottomData.getX()) * (searchTime - bottomTime) / (topTime - bottomTime);
            }

            topData = bottomData;
        }

        return null;
    }


    public void setVisibleRange(Double visibleStart, Double visibleEnd) {
        mIsDirty = true;
        mEnd = new DataPoint<>(0.0, visibleStart);
        mStart = new DataPoint<>(0.0, visibleEnd);
        fireUpdates();
    }
}

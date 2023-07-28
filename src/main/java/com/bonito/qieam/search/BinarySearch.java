package com.bonito.qieam.search;

import java.util.Arrays;

public class BinarySearch {

    int search(int[] arr, int x) {
        for (int i = 0; i<arr.length; i++) {
            if (arr[i] == x) {
                return i;
            }
        }
        return -1;
    }

    int binarySearch(int[] arr, int x) {
        int l = 0;
        int r = arr.length - 1;
        int[] array = Arrays.stream(arr).sorted().toArray();
        int mid = arr.length / 2;

        while (r >= l) {
            if (array[mid] == x) {
                return mid;
            }
            if (array[mid] < x) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return -1;
    }

    int rebinarySearch(int[] arr, int x) {
        int[] array = Arrays.stream(arr).sorted().toArray();
        int mid = arr.length / 2;

        if (array[mid] == x) {
            return mid;
        }

        if (array[mid] > x) {
            int[] ints = Arrays.copyOf(array, mid);
            return rebinarySearch(ints,  x);
        } else {
            int[] ints = Arrays.copyOf(array, mid-1);
            return rebinarySearch(ints, x);
        }
    }
}

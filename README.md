Sparsity
========
This pure Java library implements multiple algorithms for text classification.
It was initially designed as a part of solution for Kagle competition
http://kaggle.com/c/facebook-recruiting-iii-keyword-extraction/

This library provides
---------------------
*   Pipe framework for training and validating models with lazy data load support.
*   Efficient Sparse and Dense Matrix implementations. Dense matrices are not primary focus of this library yet, mostly because text data is sparse.
*   Efficient Off-heap sparse matrix implementation for handling data that doesn't fit into main memory.
*   Bit matrix.
*   KNN algorithm for classification
*   RandomizedPCA implementation which capable to handle large sparse matrices (it can't handle off-heap matrices though, see roadmap)
*   Locality Sensitive Hashing implementation using Random Projections


Next on the roadmap
-------------------
*   Regularized SVD to run PCA on off-heap matrices.
*   Very Sparse Random Projections
*   Parallel implementation for Pipe framework
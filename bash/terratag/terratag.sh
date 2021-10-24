#!/bin/bash 

cd $workingDirectory;
tar -xvf terratag_0.1.29_darwin_amd64.tar.gz;
chmod +x terratag;
./terratag -tags="{\"environment_id\": \"development\"}"
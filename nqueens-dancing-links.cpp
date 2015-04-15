#include <iostream>

using namespace std;

#define MAXN 16

int N,ans_c[MAXN],total;
int le[MAXN + 1],ri[MAXN + 1];
bool diag1[2 * MAXN],diag2[2 * MAXN];

void search(int r){
	if(r == N){
		/*for(int i = 0;i < N;++i){
			for(int j = 0;j < N;++j)
				if(j == ans_c[i]) cout << "x";
				else cout << "*";
			cout << "\n";
		}
		cout << endl;*/

		++total;
	}else{
		int c = ri[0];

		while(c != -1){
			if(!diag1[r + c] && !diag2[r - c + MAXN]){
				diag1[r + c] = true;
				diag2[r - c + MAXN] = true;
				ans_c[r] = c;

				if(ri[c] != -1)
					le[ ri[c] ] = le[c];
				ri[ le[c] ] = ri[c];
	
				search(r + 1);
	
				diag1[r + c] = false;
				diag2[r - c + MAXN] = false;

				if(ri[c] != -1)
					le[ ri[c] ] = c;
				ri[ le[c] ] = c;
			}

			c = ri[c];
		}
	}
}

int main(){
	for(N = 1;N <= MAXN;++N){
		for(int i = 0;i < N;++i)
			ri[i] = i + 1;
		ri[N] = -1;

		le[0] = -1;
		for(int i = 1;i <= N;++i)
			le[i] = i - 1;

		total = 0;
		search(0);
	
		cout << "total = " << total << endl;
	}

	return 0;
}
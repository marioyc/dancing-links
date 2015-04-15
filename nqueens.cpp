#include <iostream>

using namespace std;

#define MAXN 16

int N,ans_c[MAXN],total;
bool col[MAXN],diag1[2 * MAXN],diag2[2 * MAXN];

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
		for(int c = 0;c < N;++c){
			if(!col[c] && !diag1[r + c] && !diag2[r - c + MAXN]){
				col[c] = true;
				diag1[r + c] = true;
				diag2[r - c + MAXN] = true;
				ans_c[r] = c;

				search(r + 1);

				col[c] = false;
				diag1[r + c] = false;
				diag2[r - c + MAXN] = false;
			}
		}
	}
}

int main(){
	for(N = 1;N <= MAXN;++N){
		total = 0;
		search(0);
	
		cout << "total = " << total << endl;
	}

	return 0;
}
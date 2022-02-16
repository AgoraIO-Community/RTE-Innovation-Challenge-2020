#include <bits/stdc++.h>

#define rep(i,a,n) for(int i=a;i<n;i++)
#define reps(i,a,n) for(int i=a;i>n;i--)
#define edl() cout<<endl
#define ll long long
#define db(x) if(DEBUG) cout<<"{"<<#x<<"="<<(x)<<"}";

using namespace std;

const bool DEBUG=1;
const int inf = 0x7fffffff;
const int mod = (int)1e9+9;
const int N = 500015;
int n,m,l,t;

bool space[140][1300][65] = {0};
bool visit[140][1300][65] = {0};

struct bfsi {
  int x,y,z;
};

int dx[6] = {0,0,-1,1,0,0};
int dy[6] = {0,0,0,0,-1,1};
int dz[6] = {1,-1,0,0,0,0};


int main(){
  ios::sync_with_stdio(false);
	cin>>m>>n>>l>>t;
  for(int i=1;i<=l;i++) {
    for(int j=1;j<=m;j++) {
      for(int k=1;k<=n;k++) {
        cin>>space[k][j][i];
      }
    }
  }

  int head=0,tail=0;
  bfsi bfsa[100000];
  int ans = 0;
  for(int z=1;z<=l;z++) {
    for(int y=1;y<=m;y++) {//m y ; n x ; 
      for(int x=1;x<=n;x++) {
        if(space[x][y][z] && !visit[x][y][z]) {
          bfsa[tail++] = {x,y,z};
          visit[x][y][z] = 1;
          int value = 1;
          while(head<tail || tail<head) {
            bfsi tmp = bfsa[head];
            int x1=tmp.x,y1=tmp.y,z1=tmp.z;
            for(int i=0;i<6;i++) {
              if(space[x1+dx[i]][y1+dy[i]][z1+dz[i]] && 
                !visit[x1+dx[i]][y1+dy[i]][z1+dz[i]]) {
                  visit[x1+dx[i]][y1+dy[i]][z1+dz[i]] = 1;
                  if(tail+1==100000) {
                    tail = 0;
                  }
                  bfsa[tail++] = {x1+dx[i],y1+dy[i],z1+dz[i]};
                  value++;
                }
            }
            head++;
            if(head==100000) {
              head = 0;
            }
          }
          if(value>=t) {
            ans+=value;
          }
        }
      }
    }
  }
  cout<<ans;

	return 0;
}
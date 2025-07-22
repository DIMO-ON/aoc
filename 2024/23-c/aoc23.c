#include <stdio.h>
#include <stdlib.h>

#define SIZE 'z'-'a' + 1

int lan[SIZE][SIZE][SIZE][SIZE];


int hast = 0;
size_t tcount = 0;

void checkParty(char* connection)
{

    // printf("%s\n", connection);
    int a = connection[0] - 'a';
    int b = connection[1] - 'a';
    int c = connection[2] - 'a';
    int d = connection[3] - 'a';

    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            if (!lan[a][b][c][d] && !lan[c][d][a][b] &&
                 lan[a][b][i][j] && lan[c][d][i][j]) {

                hast = (a + 'a') == 't' || (b + 'a') == 't' ||
                       (c + 'a') == 't' || (d + 'a') == 't' ||
                       (i + 'a') == 't' || (j + 'a') == 't';

                // if (hast) printf("%s%c%c\n",connection,i+'a',j+'a');
                tcount += hast;
            }
            
        }
    }

    lan[a][b][c][d] = !lan[a][b][c][d];
}

int main(void)
{

    for (int a = 0; a < SIZE; a++)
        for (int b = 0; b < SIZE; b++)
            for (int c = 0; c < SIZE; c++)
                for (int d = 0; d < SIZE; d++)
                    lan[a][b][c][d] = 0;

    char* connection = (char*)calloc(sizeof(char),4);
    int c = 0;
    char l = 0;

    FILE* f = fopen("input.txt", "r");

    while ((l = getc(f)) != EOF) {
        // printf("%c", l);
        if (l == '\n') {
            if (connection[0] && connection[3])
                checkParty(connection);

            for (int i = 0; i <= 4; i++)
                connection[i] = 0;
            hast = 0;
            c = 0;
        }

        else if (l <= 'z' && l >= 'a') {
            connection[c] = l;
            c++;
        }
    }

    printf("\n\n\nt connections: %d\n\n", tcount);

    free(connection);

    return 0;

}

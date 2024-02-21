#include <stdio.h>
#include <string.h>

#define MAX_BUF  256
#define IV 0b11001011

unsigned char initKey();
unsigned char processKey(unsigned char);

void encode(unsigned char*, unsigned char*, unsigned char, int);
void decode(unsigned char*, unsigned char*, unsigned char, int);

unsigned char encryptByte(unsigned char, unsigned char, unsigned char);
unsigned char decryptByte(unsigned char, unsigned char, unsigned char);
unsigned char cShiftRight(unsigned char);
unsigned char cShiftLeft(unsigned char);

int readBytes(unsigned char*, int);
unsigned char getBit(unsigned char, int);
unsigned char setBit(unsigned char, int);
unsigned char clearBit(unsigned char, int);


int main()
{
//in need of a plain text array
//in need of a cyphertxt array
//a local to store the key
  char str[8];
  int  choice;
  unsigned char pt[MAX_BUF]={0};
  unsigned char ct[MAX_BUF]={0};
  unsigned char key=0;

  printf("\nYou may:\n");
  printf("  (1) Encrypt a message \n");
  printf("  (2) Decrypt a message \n");
  printf("  (0) Exit\n");
  printf("\n  what is your selection: ");
  fgets(str, sizeof(str), stdin);
  sscanf(str, "%d", &choice);


  switch (choice) {

    case 1:
    
    key= initKey();
    printf( "\n Enter your message please: \n");
    readBytes(pt,MAX_BUF);
    encode(pt,ct,key,strlen((char *)pt));
    
      break;

    case 2:
    
    key = initKey();
    printf( "\n Enter your message please: \n");
    int count=0;
    int check;
    while(1){
    scanf("%d", &check);
    if(check == -1){break;}
    ct[count]= (unsigned char)check;
    count +=1;
    }
    printf("\n");
    decode(ct,pt,key,(count));
    
      break;

    default:

      break;
  }

  return(0);
}
/*
  Function:  encode
  Purpose:   encrypt each character in the plain text array using the given key
      out:   the ct array will store an encrypted byte for each corresponding byte of plain text
       in:   an array of plaintext characters (stored in pt)
   return:   is void (none).
*/
void encode(unsigned char *pt, unsigned char *ct, unsigned char key, int numBytes){
 
  //key= processKey(key);
  unsigned char prev=IV;
  
  for(int i = 0; i < numBytes; i++){
  //printf("!!\n");
    key = processKey(key);
    ct[i]= encryptByte(pt[i],key, prev);
    prev=ct[i];
  }
  
  for(int i=0;i < strlen((char *)pt);i++){
    printf("%03d ",ct[i]);
    }
  
    
}

/*
  Function:  decode
  Purpose:   decrypt each character in the plain text array using the given key
      out:   the pt array will store an decrypted byte for each corresponding byte of cipher text
       in:   an array of ciphertext characters (stored in ct)
   return:   is void (none).
*/
void decode(unsigned char *ct, unsigned char *pt, unsigned char key, int numBytes){

key=processKey(key);

  unsigned char prev=IV;
  
  for(int i = 0; i < numBytes; i ++){
  //printf("dec\n");
    pt[i] = decryptByte(ct[i],key, prev);
    prev  =ct[i];
    key   =processKey(key);
  }
  
  //for(int i =0;i<numBytes;i++){
  printf("%s",pt);
  //
}

/*
  Function:  encryptByte
  Purpose:   encrypt the plaintext one byte at a time
      out:   corresponding ecnrypted ciphertext byte
       in:   the key and the previous byte of cyphertxt
   return:   corresponding encrypted cyphertext byte
*/
unsigned char encryptByte(unsigned char pt, unsigned char key, unsigned char prev){


int xorResult;
unsigned char tmp=0;

  for(int i = 0; i < 8; i++){
 // printf("encrypting\n");
    if(getBit(key,i)== 1){ prev = cShiftRight(prev);}
    else{}
    xorResult = (getBit(pt,i) ^ getBit(prev,((-i)+7)));
    if(xorResult == 0){tmp = clearBit(tmp,i);  }
    else { tmp = setBit(tmp,i); }
  }
  return tmp;
}

/*
  Function:  decryptByte
  Purpose:   decrypt the ciphertext one byte at a time
      out:   corresponding decrypted plaintextext byte
       in:   the key and the previous byte of plaintxt
   return:   corresponding decrypted plaintext byte
*/
unsigned char decryptByte(unsigned char ct, unsigned char key, unsigned char prev){



unsigned char tmp=0;

  for(int i = 0; i <8; i++){
 // printf("encrypting\n");
    if(getBit(key,i)== 1){ prev = cShiftRight(prev);}
    else{}
    char xorResult= (getBit(ct,i) ^ getBit(prev,((-i)+7)));
    if(xorResult == 0){tmp = clearBit(tmp,i);  }
    else { tmp= setBit(tmp,i); }
  }
  return tmp;
}


/*
  Function:  readBytes
  Purpose:   reads characters from the standard output
      out:   the buffer containing the bytes read
       in:   the capacity of the buffer (maximum size)
   return:   the number of bytes actually read from the user
*/
int readBytes(unsigned char* buffer, int max)
{
  int num = 0;

  fgets((char*)buffer, max, stdin);
  num = strlen((char*)buffer) - 1;
  buffer[num] = '\0';

  return num;
}
/*
  Function:  cShiftRight
  Purpose:   completes a circular right shift on a byte
      out:   The byte with a right circular shift
       in:   the byte to be modified
   return:   an unsigned char that is a right circular shift on the original value
*/
unsigned char cShiftRight(unsigned char n){

 return (n >> 1) | (n << (8- 1));
 
}

/*
  Function:  cShiftLeft
  Purpose:   completes a circular left shift on a byte
      out:   The byte with a left circular shift
       in:   the byte to be modified
   return:   an unsigned char that is a left circular shift on the original value
*/
unsigned char cShiftLeft(unsigned char n){

 return (n << 1) | (n >> (8- 1));
 
}

/*
  Function:  initKey
  Purpose:   initlializes a key value from a partial key input
      out:   the key value
       in:   partial key value through scanf
   return:   the key value
*/
unsigned char initKey(){
 
 unsigned char key;
 int pKey;
 
 printf("Please enter the key: (an int between 1-15 inclusive)");
 scanf("%d", &pKey);
 getchar();
  while((pKey>15)||(pKey<1)){
   printf("INVALID INPUT. choose a number between 1-15 inclusive");
   scanf("%d", &pKey);
   getchar();
 }
 pKey = pKey << 4;
 int pos;
 int val;
 
 for(int i = 7; i > 3 ; i --){
   pos= ((i-1) % 4);
   val= getBit(pKey,i);
   if(val == 0){ pKey= setBit(pKey,pos); }
   else if(val == 1) { pKey=clearBit (pKey, pos);}
}

 key = pKey;
 return key;
 
}

/*
  Function:  processKey
  Purpose:   updates the key from it's current value
      out:   the updated key
       in:   the current key value
   return:   the updated key
*/
unsigned char processKey(unsigned char currKey){

  if(currKey % 3 == 0){
  for(int i=3;i>0;i--){ currKey= cShiftLeft(currKey);}
  }
  else{
  for(int i=2;i>0;i--){ currKey= cShiftLeft(currKey);}
  }
  
return currKey;
}

/*
  Function:  getBit
  Purpose:   retrieve value of bit at specified position
       in:   character from which a bit will be returned
       in:   position of bit to be returned
   return:   value of bit n in character c (0 or 1)
*/
unsigned char getBit(unsigned char c, int n)
{
 return( (c & (1 << n)) >> n );
}

/*
  Function:  setBit
  Purpose:   set specified bit to 1
       in:   character in which a bit will be set to 1
       in:   position of bit to be set to 1
   return:   new value of character c with bit n set to 1
*/
unsigned char setBit(unsigned char c, int n)
{
 return( c | (1 << n) );
}

/*
  Function:  clearBit
  Purpose:   set specified bit to 0
       in:   character in which a bit will be set to 0
       in:   position of bit to be set to 0
   return:   new value of character c with bit n set to 0
*/
unsigned char clearBit(unsigned char c, int n)
{
return( c & (~(1 << n)) );
}


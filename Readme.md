The reason why I want to make maplestory by my self is for playing v83 version.
However, client source codes of v83 all are made for GMS. And none of them support chinese and difficult to build for other platform.

After I read `JournalStory` which inspires me that maybe I can write it by my self in java, because maplestory don't need high performance.
And there is no need other library for multi-language support.
Not only that, like opengl library also can be solved by libGDX one package.

Therefore, I could develop a platform independent clean MS by Java by using libGDX.


Thank JournalStory again, I write follow the idea of that.

Any thing totally in java. Music used jmp123 NX use NolifeNX but write by my self(not test all but test several bitmap and string, and change string format to UTF read, can't compatible with original NolifeNX. No license now, if necessary, please follow jmp123 license.

现在这个项目已经完成了对于WZ文件的转码，转化为支持UTF格式的nx文件，因此可以天然对各种语言版本的Wz进行支持。

整体上，我想完全抛开journalStory的思路，对于鼠标控制直接由组件直接注册actor而不是通过一层层往下传鼠标操作信号，我认为耦合度有点高，每次都要实现cend_cur的函数，并且可读性也很低，不如直接在组件内根据自己需要的操作直接绑定actor

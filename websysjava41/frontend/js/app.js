const API_BASE = 'http://localhost:8080/api';
const WS_BASE = 'ws://localhost:8080/ws/chat';

new Vue({
    el: '#app',
    data: {
        // 登录状态
        isLoggedIn: false,
        currentUser: null,
        authTab: 'login',
        
        // 登录表单
        loginForm: {
            username: '',
            password: ''
        },
        
        // 注册表单
        registerForm: {
            username: '',
            password: '',
            nickname: ''
        },
        
        // 好友相关
        friends: [],
        friendRequests: [],
        selectedFriend: null,
        searchKeyword: '',
        searchResults: [],
        
        // 消息相关
        messages: [],
        messageInput: '',
        showEmojiPicker: false,
        emojis: ['😀', '😂', '🥰', '😎', '🤔', '😴', '🥳', '😇', '🤗', '🤩', '😊', '😁', '😍', '🥺', '😭', '😤', '😡', '🤬', '😈', '👿', '💀', '☠️', '💩', '🤡', '👹', '👺', '👻', '👽', '👾', '🤖'],
        
        // WebSocket
        websocket: null,
        
        // 未读消息计数
        unreadCounts: {}
    },
    
    mounted() {
        // 检查本地存储的登录状态
        const savedUser = localStorage.getItem('chatUser');
        if (savedUser) {
            this.currentUser = JSON.parse(savedUser);
            this.isLoggedIn = true;
            this.initChat();
        }
    },
    
    methods: {
        // 登录
        async handleLogin() {
            try {
                const response = await axios.post(`${API_BASE}/user/login`, this.loginForm);
                if (response.data.code === 200) {
                    this.currentUser = response.data.data;
                    this.isLoggedIn = true;
                    localStorage.setItem('chatUser', JSON.stringify(this.currentUser));
                    this.loginForm = { username: '', password: '' };
                    this.initChat();
                } else {
                    alert(response.data.message || '登录失败');
                }
            } catch (error) {
                alert('登录失败: ' + (error.response?.data?.message || error.message));
            }
        },
        
        // 注册
        async handleRegister() {
            try {
                const response = await axios.post(`${API_BASE}/user/register`, this.registerForm);
                if (response.data.code === 200) {
                    alert('注册成功，请登录');
                    this.authTab = 'login';
                    this.registerForm = { username: '', password: '', nickname: '' };
                } else {
                    alert(response.data.message || '注册失败');
                }
            } catch (error) {
                alert('注册失败: ' + (error.response?.data?.message || error.message));
            }
        },
        
        // 登出
        async handleLogout() {
            try {
                await axios.post(`${API_BASE}/user/logout/${this.currentUser.id}`);
            } catch (e) {}
            
            if (this.websocket) {
                this.websocket.close();
            }
            
            this.isLoggedIn = false;
            this.currentUser = null;
            this.friends = [];
            this.friendRequests = [];
            this.messages = [];
            this.selectedFriend = null;
            localStorage.removeItem('chatUser');
        },
        
        // 初始化聊天
        initChat() {
            this.loadFriends();
            this.loadFriendRequests();
            this.connectWebSocket();
        },
        
        // 连接WebSocket
        connectWebSocket() {
            this.websocket = new WebSocket(`${WS_BASE}/${this.currentUser.id}`);
            
            this.websocket.onopen = () => {
                console.log('WebSocket连接成功');
            };
            
            this.websocket.onmessage = (event) => {
                const message = JSON.parse(event.data);
                // 如果是当前选中的好友发来的消息，直接添加到消息列表
                if (this.selectedFriend && 
                    (message.fromUserId === this.selectedFriend.id || 
                     message.fromUserId === this.currentUser.id)) {
                    this.messages.push(message);
                    this.$nextTick(() => {
                        this.scrollToBottom();
                    });
                    // 标记为已读
                    if (message.fromUserId === this.selectedFriend.id) {
                        this.markAsRead(this.selectedFriend.id);
                    }
                } else {
                    // 更新未读计数
                    if (message.fromUserId !== this.currentUser.id) {
                        this.unreadCounts[message.fromUserId] = (this.unreadCounts[message.fromUserId] || 0) + 1;
                    }
                }
            };
            
            this.websocket.onerror = (error) => {
                console.error('WebSocket错误:', error);
            };
            
            this.websocket.onclose = () => {
                console.log('WebSocket连接关闭');
                // 尝试重连
                setTimeout(() => {
                    if (this.isLoggedIn) {
                        this.connectWebSocket();
                    }
                }, 3000);
            };
        },
        
        // 加载好友列表
        async loadFriends() {
            try {
                const response = await axios.get(`${API_BASE}/friend/list/${this.currentUser.id}`);
                if (response.data.code === 200) {
                    this.friends = response.data.data;
                }
            } catch (error) {
                console.error('加载好友列表失败:', error);
            }
        },
        
        // 加载好友请求
        async loadFriendRequests() {
            try {
                const response = await axios.get(`${API_BASE}/friend/requests/${this.currentUser.id}`);
                if (response.data.code === 200) {
                    this.friendRequests = response.data.data;
                }
            } catch (error) {
                console.error('加载好友请求失败:', error);
            }
        },
        
        // 搜索用户
        async searchUsers() {
            if (!this.searchKeyword.trim()) {
                this.searchResults = [];
                return;
            }
            
            try {
                const response = await axios.get(`${API_BASE}/user/search`, {
                    params: { nickname: this.searchKeyword }
                });
                if (response.data.code === 200) {
                    // 过滤掉自己和已经是好友的用户
                    const friendIds = this.friends.map(f => f.id);
                    this.searchResults = response.data.data.filter(u => 
                        u.id !== this.currentUser.id && !friendIds.includes(u.id)
                    );
                }
            } catch (error) {
                console.error('搜索用户失败:', error);
            }
        },
        
        // 添加好友
        async addFriend(friendId) {
            try {
                const response = await axios.post(`${API_BASE}/friend/request`, null, {
                    params: {
                        userId: this.currentUser.id,
                        friendId: friendId
                    }
                });
                if (response.data.code === 200) {
                    alert('好友请求已发送');
                    this.searchResults = this.searchResults.filter(u => u.id !== friendId);
                } else {
                    alert(response.data.message);
                }
            } catch (error) {
                alert('发送好友请求失败: ' + (error.response?.data?.message || error.message));
            }
        },
        
        // 接受好友请求
        async acceptFriend(requestId) {
            try {
                const response = await axios.post(`${API_BASE}/friend/accept/${requestId}`);
                if (response.data.code === 200) {
                    alert('已添加好友');
                    this.loadFriendRequests();
                    this.loadFriends();
                } else {
                    alert(response.data.message);
                }
            } catch (error) {
                alert('接受好友请求失败: ' + (error.response?.data?.message || error.message));
            }
        },
        
        // 拒绝好友请求
        async rejectFriend(requestId) {
            try {
                const response = await axios.post(`${API_BASE}/friend/reject/${requestId}`);
                if (response.data.code === 200) {
                    alert('已拒绝好友请求');
                    this.loadFriendRequests();
                } else {
                    alert(response.data.message);
                }
            } catch (error) {
                alert('拒绝好友请求失败: ' + (error.response?.data?.message || error.message));
            }
        },
        
        // 选择好友
        async selectFriend(friend) {
            this.selectedFriend = friend;
            this.messages = [];
            
            // 加载聊天记录
            await this.loadChatHistory(friend.id);
            
            // 标记为已读
            await this.markAsRead(friend.id);
            
            // 清除未读计数
            this.unreadCounts[friend.id] = 0;
        },
        
        // 加载聊天记录
        async loadChatHistory(friendId) {
            try {
                const response = await axios.get(`${API_BASE}/chat/history`, {
                    params: {
                        userId1: this.currentUser.id,
                        userId2: friendId,
                        page: 0,
                        size: 50
                    }
                });
                if (response.data.code === 200) {
                    this.messages = response.data.data.reverse();
                    this.$nextTick(() => {
                        this.scrollToBottom();
                    });
                }
            } catch (error) {
                console.error('加载聊天记录失败:', error);
            }
        },
        
        // 标记为已读
        async markAsRead(friendId) {
            try {
                await axios.post(`${API_BASE}/chat/mark-read`, null, {
                    params: {
                        fromUserId: friendId,
                        toUserId: this.currentUser.id
                    }
                });
            } catch (error) {
                console.error('标记已读失败:', error);
            }
        },
        
        // 获取未读消息数
        getUnreadCount(friendId) {
            return this.unreadCounts[friendId] || 0;
        },
        
        // 发送消息
        sendMessage() {
            if (!this.messageInput.trim() || !this.selectedFriend) return;
            
            const message = {
                fromUserId: this.currentUser.id,
                toUserId: this.selectedFriend.id,
                type: 0,
                content: this.messageInput
            };
            
            if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
                this.websocket.send(JSON.stringify(message));
                this.messageInput = '';
            } else {
                // WebSocket未连接，使用REST方式发送
                this.sendMessageViaRest(message);
            }
        },
        
        // 使用REST发送消息
        async sendMessageViaRest(message) {
            try {
                const response = await axios.post(`${API_BASE}/chat/send`, null, {
                    params: {
                        fromUserId: message.fromUserId,
                        toUserId: message.toUserId,
                        type: message.type,
                        content: message.content
                    }
                });
                if (response.data.code === 200) {
                    this.messages.push(response.data.data);
                    this.messageInput = '';
                    this.$nextTick(() => {
                        this.scrollToBottom();
                    });
                }
            } catch (error) {
                alert('发送消息失败: ' + (error.response?.data?.message || error.message));
            }
        },
        
        // 选择表情
        selectEmoji(emoji) {
            this.messageInput += emoji;
            this.showEmojiPicker = false;
        },
        
        // 触发图片上传
        triggerImageUpload() {
            this.$refs.imageInput.click();
        },
        
        // 触发文件上传
        triggerFileUpload() {
            this.$refs.fileInput.click();
        },
        
        // 处理图片上传
        async handleImageUpload(event) {
            const file = event.target.files[0];
            if (!file) return;
            
            try {
                const formData = new FormData();
                formData.append('file', file);
                
                const response = await axios.post(`${API_BASE}/chat/upload`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
                
                if (response.data.code === 200) {
                    const message = {
                        fromUserId: this.currentUser.id,
                        toUserId: this.selectedFriend.id,
                        type: 1,
                        fileName: file.name,
                        fileUrl: response.data.data
                    };
                    
                    if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
                        this.websocket.send(JSON.stringify(message));
                    }
                }
            } catch (error) {
                alert('上传失败: ' + (error.response?.data?.message || error.message));
            }
            
            event.target.value = '';
        },
        
        // 处理文件上传
        async handleFileUpload(event) {
            const file = event.target.files[0];
            if (!file) return;
            
            try {
                const formData = new FormData();
                formData.append('file', file);
                
                const response = await axios.post(`${API_BASE}/chat/upload`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
                
                if (response.data.code === 200) {
                    const message = {
                        fromUserId: this.currentUser.id,
                        toUserId: this.selectedFriend.id,
                        type: 2,
                        fileName: file.name,
                        fileUrl: response.data.data,
                        fileSize: file.size
                    };
                    
                    if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
                        this.websocket.send(JSON.stringify(message));
                    }
                }
            } catch (error) {
                alert('上传失败: ' + (error.response?.data?.message || error.message));
            }
            
            event.target.value = '';
        },
        
        // 滚动到底部
        scrollToBottom() {
            const messageList = this.$refs.messageList;
            if (messageList) {
                messageList.scrollTop = messageList.scrollHeight;
            }
        },
        
        // 格式化时间
        formatTime(timeStr) {
            if (!timeStr) return '';
            const date = new Date(timeStr);
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            return `${hours}:${minutes}`;
        }
    }
});
